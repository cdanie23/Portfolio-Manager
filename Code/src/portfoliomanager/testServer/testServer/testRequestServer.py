'''
Created on Mar 25, 2025

@author: Aayush
'''
import unittest
import zmq
import time
import json
from request_server import constants, server
from request_server.request_handler import RequestHandler
from threading import Thread

from request_server.crypto_metrics import CryptoMetric
from unittest.mock import MagicMock, patch

class MockTrendHolder:
    def __init__(self, curr_trend):
        self.curr_trend = CryptoMetric(curr_trend)

    def getCurrTrend(self):
        return self.curr_trend


class TestRequestServer(unittest.TestCase):
    
    def setUp(self):
        mocked_trend = [
        {"id": "bitcoin", "symbol": "btc", "name": "Bitcoin", "current_price": 60000, "total_volume": 30000},
        {"id": "ethereum", "symbol": "eth", "name": "Ethereum", "current_price": 3000, "total_volume": 15000}
        ]
        self.mock_trend_holder = MockTrendHolder(mocked_trend)
        serverThread = Thread(target=lambda: server.runServer(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT, self.mock_trend_holder))
        serverThread.daemon = True
        serverThread.start()
        time.sleep(1)
        self._context = zmq.Context()
        self._socket = self._context.socket(zmq.REQ)
        serverLocation = "{0}://{1}:{2}".format(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT)
        self._socket.connect(serverLocation)
        
    def tearDown(self):
        self._socket.send_string(json.dumps({constants.KEY_REQUEST_TYPE : constants.EXIT}))
        self._socket.close()
        self._context.term()
        
    def _login(self):
        loginRequest = {
            constants.KEY_REQUEST_TYPE:"login", 
            "username":"user", 
            "password":"pass123"}
        self._socket.send_string(json.dumps(loginRequest))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        auth_token = response.get(constants.KEY_TOKEN)
        return auth_token
    
    def testUnsupportedRequestType(self):
        request = {constants.KEY_REQUEST_TYPE:-33}
        jsonRequest = json.dumps(request)
    
        self._socket.send_string(jsonRequest)
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.UNSUPPORTED_OPERATION_STATUS, response[constants.KEY_STATUS], "checking status of response")
    
    def testNoRequestType(self):
        jsonRequest = json.dumps({"hello" : "world"})
    
        self._socket.send_string(jsonRequest)
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS], "checking status of response")
    
    def testHandleSignUp(self):
        signupRequest = {
            constants.KEY_REQUEST_TYPE: "signUp",
            "username": "user1",
            "password": "pass123",
            "confirmPassword": "pass123"
        }
        self._socket.send_string(json.dumps(signupRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking status of signUp response")
    
    def testHandleSignUpNullName(self):
        signupRequest = {
            constants.KEY_REQUEST_TYPE: "signUp",
            "username": None,
            "password": "pass123",
            "confirmPassword": "pass123"
        }
        self._socket.send_string(json.dumps(signupRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking signUp failure with incorrect credentials")
    
    def testHandleSignUpIncorrectConfirmPassword(self):
        signupRequest = {
            constants.KEY_REQUEST_TYPE: "signUp",
            "username": "user2",
            "password": "pass123",
            "confirmPassword": "pass1234"
        }
        self._socket.send_string(json.dumps(signupRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking signUp failure with incorrect credentials")
    
    def testHandleSignUpDuplicateUser(self):
        signupRequest = {
            constants.KEY_REQUEST_TYPE: "signUp",
            "username": "user",
            "password": "pass123",
            "confirmPassword": "pass123"
        }
        self._socket.send_string(json.dumps(signupRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS], "Checking signUp failure with incorrect credentials")
    
    def testHandleLogin(self):
        loginRequest = {
            constants.KEY_REQUEST_TYPE: "login",
            "username": "user",
            "password": "pass123"
        }
        self._socket.send_string(json.dumps(loginRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking status of login response")
    
    def testHandleLoginNullName(self):
        loginRequest = {
            constants.KEY_REQUEST_TYPE: "login",
            "username": None,
            "password": "pass123"
        }
        self._socket.send_string(json.dumps(loginRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking login failure with incorrect credentials")
    
    def testHandleLoginNoSavedUser(self):
        loginRequest = {
            constants.KEY_REQUEST_TYPE: "login",
            "username": "user5",
            "password": "pass123"
        }
        self._socket.send_string(json.dumps(loginRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking login failure with incorrect credentials")
    
    def testHandleGetHoldings(self):
        auth_token = self._login()
        get_holding_request = {
            constants.KEY_TOKEN: auth_token, 
            constants.KEY_REQUEST_TYPE: constants.GET_HOLDINGS}
        self._socket.send_string(json.dumps(get_holding_request))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
    
    def testGetHoldingsNoAuth(self):
        get_holding_request = {
            constants.KEY_TOKEN: None, 
            constants.KEY_REQUEST_TYPE: constants.GET_HOLDINGS}
        self._socket.send_string(json.dumps(get_holding_request))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testModifyHolding(self):
        auth_token = self._login()
        add_holding_request= {
            constants.KEY_AMOUNT: 50.2,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_NAME: 'BTC-USD',
            constants.KEY_REQUEST_TYPE: constants.GET_BUY,
            constants.KEY_FUNDS: 5.0
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
    
    def testModifyHoldingSelling(self):
        auth_token = self._login()
        add_holding_request= {
            constants.KEY_AMOUNT: 50.2,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_NAME: 'BTC-USD',
            constants.KEY_REQUEST_TYPE: constants.GET_SELL,
            constants.KEY_FUNDS: 5.0
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
    
    def testModifyHoldingsNoAuth(self):
        add_holding_request= {
            constants.KEY_AMOUNT: 50.2,
            constants.KEY_TOKEN: None,
            constants.KEY_NAME: 'btc',
            constants.KEY_REQUEST_TYPE: constants.GET_BUY,
            constants.KEY_FUNDS: 5.0
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testModifyHoldingsNoCryptoName(self):
        auth_token = self._login()
        add_holding_request= {
            constants.KEY_AMOUNT: 50.2,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_NAME: None,
            constants.KEY_REQUEST_TYPE: constants.GET_BUY,
            constants.KEY_FUNDS: 5.0
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testModifyHoldingsNoAmount(self):
        auth_token = self._login()
        add_holding_request= {
            constants.KEY_AMOUNT: None,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_NAME: 'btc',
            constants.KEY_REQUEST_TYPE: constants.GET_BUY,
            constants.KEY_FUNDS: 5.0
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testModifyHoldingsNoTotalCost(self):
        auth_token = self._login()
        add_holding_request= {
            constants.KEY_AMOUNT: 5,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_NAME: 'btc',
            constants.KEY_REQUEST_TYPE: constants.GET_BUY,
            constants.KEY_FUNDS: None
            }
    
        self._socket.send_string(json.dumps(add_holding_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testAddFunds(self):
        auth_token = self._login()
        add_funds_request = {
            constants.KEY_AMOUNT: 20.0,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_REQUEST_TYPE: constants.ADD_FUNDS
            }
    
        self._socket.send_string(json.dumps(add_funds_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
    
    def testAddFundsNoAuthToken(self):
        add_funds_request = {
            constants.KEY_AMOUNT: 20.0,
            constants.KEY_TOKEN: None,
            constants.KEY_REQUEST_TYPE: constants.ADD_FUNDS
            }
    
        self._socket.send_string(json.dumps(add_funds_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testAddFundsNoAmount(self):
        auth_token = self._login()
        add_funds_request = {
            constants.KEY_AMOUNT: None,
            constants.KEY_TOKEN: auth_token,
            constants.KEY_REQUEST_TYPE: constants.ADD_FUNDS
            }
    
        self._socket.send_string(json.dumps(add_funds_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testGetFunds(self):
        auth_token = self._login()
        get_funds_request = {
            constants.KEY_TOKEN: auth_token,
            constants.KEY_REQUEST_TYPE: constants.GET_FUNDS
            }
        self._socket.send_string(json.dumps(get_funds_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
    
    def testGetFundsNoAuth(self):
        get_funds_request = {
            constants.KEY_TOKEN: None,
            constants.KEY_REQUEST_TYPE: constants.GET_FUNDS
            }
        self._socket.send_string(json.dumps(get_funds_request))
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
    
    def testMakeAccount(self):
        _request_Handler = RequestHandler(self.mock_trend_holder)
        _request_Handler.makeAccount("test_user", "test")
    
        self.assertEqual(2, len(_request_Handler._users))
    
    
    
    def testHandleLogout(self):
        signupRequest = {
            constants.KEY_REQUEST_TYPE: "signUp",
            "username": "user1",
            "password": "pass123",
            "confirmPassword": "pass123"
        }
        self._socket.send_string(json.dumps(signupRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        token = response.get(constants.KEY_TOKEN)
    
        logoutRequest = {
            constants.KEY_REQUEST_TYPE: "logout",
            "token": token
        }
        self._socket.send_string(json.dumps(logoutRequest))
    
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Logout should succeed")
    
    def testHandleLogoutInvalidToken(self):
        logoutRequest = {
            constants.KEY_REQUEST_TYPE: "logout",
            constants.KEY_TOKEN: "12345"
        }
        self._socket.send_string(json.dumps(logoutRequest))
        jsonResponse =  self._socket.recv_string()
        response = json.loads(jsonResponse)
    
        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Logout with invalid token should fail")
    
    def testGetAllCryptoData(self):
        request = {"type" : "getData"}
        self._socket.send_string(json.dumps(request))
        jsonResponse =  self._socket.recv_string()
        response = json.loads(jsonResponse)
        cryptoData = response["data"]
        successCode = response["success code"]
        self.assertEqual(1, successCode)
        self.assertIsInstance(cryptoData, dict)
    
    
    def testHandlePriceRequest(self):
        request= {constants.KEY_REQUEST_TYPE: "getPrice",
                  constants.GET_CRYPTO_NAME: "bitcoin"
                  }
        self._socket.send_string(json.dumps(request))
        jsonResponse =  self._socket.recv_string()
        response = json.loads(jsonResponse)
        cryptoPrice = response["price"]
        successCode = response["success code"]
        self.assertEqual(1, successCode)
        self.assertIsInstance(cryptoPrice, float)
    
    def testHandlePriceRequestNoName(self):
        request= {constants.KEY_REQUEST_TYPE: "getPrice",
                  constants.GET_CRYPTO_NAME: None }
        self._socket.send_string(json.dumps(request))
        jsonResponse =  self._socket.recv_string()
        response = json.loads(jsonResponse)
        successCode = response["success code"]
        self.assertEqual(-1, successCode)
    
    def testCmGetHistoricalData(self):
        _request_Handler = RequestHandler(self.mock_trend_holder)
        cryptoData = _request_Handler.trend.getCurrTrend().getHistoricalData("bitcoin")
        self.assertIsInstance(cryptoData, dict)
    
    def testStartTrendUpdateCallsUpdateTrend(self):
        mock_trend_holder = self.mock_trend_holder
        mock_trend_holder.updateTrend = MagicMock()
        original_sleep = time.sleep
        time.sleep = lambda _: original_sleep(0.1)
    
        try:
            server.startTrendUpdate(mock_trend_holder)
            time.sleep(0.3)
    
            self.assertGreaterEqual(
                mock_trend_holder.updateTrend.call_count,
                1,
            )
        finally:
            time.sleep = original_sleep

    @patch("request_server.server.runServer")
    @patch("request_server.server.startTrendUpdate")
    def test_main_runs_server_and_updater(self, mock_updater, mock_runserver):
        #from request_server import server
        server.main()
        mock_updater.assert_called_once()
        mock_runserver.assert_called_once()
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()