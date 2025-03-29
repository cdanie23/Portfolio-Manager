'''
Created on Mar 25, 2025

@author: Aayush
'''
import unittest
import zmq
import time
import json
from request_server import constants, server
from threading import Thread
from _overlapped import NULL

class TestRequestServer(unittest.TestCase):
    
    def setUp(self):
        serverThread = Thread(target=server.runServer, args=(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT,))
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
        time.sleep(10)
        
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
        
    def testGetCurrBtcPrice(self):
        jsonRequest = json.dumps({constants.KEY_REQUEST_TYPE : constants.GET_BTC_CURR_PRICE})
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "checking status of response")
        
    def testGetBtcPriceHistory(self):
        jsonRequest = json.dumps({constants.KEY_REQUEST_TYPE : constants.GET_BTC_PRICE_HISTORY})
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "checking status of response")
    
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
            "username": NULL,
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

        self.assertNotEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "Checking signUp failure with incorrect credentials")
    
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
            "username": NULL,
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
        
if __name__ == "__main__":
    unittest.main()