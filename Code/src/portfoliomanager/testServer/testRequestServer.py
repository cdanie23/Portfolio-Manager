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

class TestRequestServer(unittest.TestCase):
    
    def setUp(self):
        serverThread = Thread(target=server.runServer, args=(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT,))
        serverThread.start()
        time.sleep(1)
        context = zmq.Context()
        self._socket = context.socket(zmq.REQ)
        serverLocation = "{0}://{1}:{2}".format(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT)
        self._socket.connect(serverLocation)
        
    def tearDown(self):
        self._socket.send_string(json.dumps("exit"))
        
        
    def testUnsupportedRequestType(self):
        request = {constants.KEY_REQUEST_TYPE:-33}
        jsonRequest = json.dumps(request)
        
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        
        
        self.assertEqual(constants.UNSUPPORTED_OPERATION_STATUS, response[constants.KEY_STATUS], "checking status of response")
        
    def testNoRequestType(self):
        jsonRequest = json.dumps("")
        
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        
        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS], "checking status of response")
        
    def testHandleRequest(self):
        jsonRequest = "{\"type\": \"cryptos\"}";
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "checking status of response")
        
    def testGetCurrBtcPrice(self):
        jsonRequest = "{\"type\": \"BTC price\"}";
        self._socket.send_string(jsonRequest)
        
        jsonResponse = self._socket.recv_string()
        response = json.loads(jsonResponse)
        
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS], "checking status of response")
        
if __name__ == "__main__":
    unittest.main()