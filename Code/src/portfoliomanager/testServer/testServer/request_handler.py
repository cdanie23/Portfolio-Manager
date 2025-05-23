'''
Created on Mar 25, 2025

@author: Aayush
'''
import unittest

from request_server.request_handler import RequestHandler
from request_server.crypto_metrics import CryptoMetric

class MockTrendHolder:
    def __init__(self, curr_trend):
        self.curr_trend = CryptoMetric(curr_trend)

    def getCurrTrend(self):
        return self.curr_trend

class TestRequestHandler(unittest.TestCase):
    def setUp(self):
        mocked_trend = [
        {"id": "bitcoin", "symbol": "btc", "name": "Bitcoin", "current_price": 60000, "total_volume": 30000},
        {"id": "ethereum", "symbol": "eth", "name": "Ethereum", "current_price": 3000, "total_volume": 15000}
        ]
        self.mock_trend_holder = MockTrendHolder(mocked_trend)
        self.requestHandler = RequestHandler(self.mock_trend_holder)

    def testConstructor(self):
        self.assertFalse(not self.requestHandler._users)
        self.assertFalse(not self.requestHandler._tokens)
        self.assertTrue(not self.requestHandler._cryptos)
    def testAddFundsToUnknownAccount(self):
        request = {
            "token" : "$7777777",
            "amount" : 1,
            "name" : "bitcoin"
            }
        response = self.requestHandler.handleAddFunds(request)
        self.assertEqual(-1, response["success code"])
        self.assertEqual("account with token not found", response["error description"])
    
    def testAddFundsToKnownAccount(self):
        request = {
            "token" : "$123",
            "amount" : 1,
            "name" : "Bitcoin"
            }  
        response = self.requestHandler.handleAddFunds(request)
        
        self.assertEqual(1, response["success code"])
        self.assertEqual("$123", response["token"])
    def testHandleGetFundsForKnownAccount(self):
        request = {
            "type" : "getFunds",
            "token" : "$123"
            }
        response = self.requestHandler.handleGetFunds(request)
        self.assertEqual(response["amount"], 0)
        self.assertEqual(response["success code"], 1)
    def testHandleGetFundsForUnknownAccount(self):
        request = {
            "type" : "getFunds",
            "token" : "$77777"
            }
        response = self.requestHandler.handleGetFunds(request)
        self.assertEqual(response["error description"], "account with token not found")
        self.assertEqual(response["success code"], -1)   
    def testFindAccountByValidAuth(self):
        account = self.requestHandler.findAccountByAuth("$123")
        self.assertFalse(not account)
    def testFindAccountByInvalidAuth(self):
        account = self.requestHandler.findAccountByAuth("$&&&&&")
        self.assertTrue(not account)
    def testFindAccountByValidName(self): 
        account = self.requestHandler.findAccountByName("user", "pass123")
        self.assertFalse(not account)
    def testFindAccountByInvalidName(self): 
        account = self.requestHandler.findAccountByName("nottUSer", "notpass")
        self.assertTrue(not account)
    def testGetAllCryptoData(self):
        response = self.requestHandler.handleGetAllCryptoData()
        cryptoData = response["data"]
        self.assertEqual(1, response["success code"])
        self.assertFalse(not cryptoData)
        self.assertIsInstance(cryptoData, dict, "check if a dictionary")
    def testGetAllCryptoDataWhenFileDoesNotExist(self):
        response = self.requestHandler.handleGetAllCryptoData("Wrong/path/file")
        self.assertEqual(response["success code"], -1)
        self.assertEqual(response["error description"], "Empty crypto data")
if (__name__ == "__main__"):
    unittest.main()  