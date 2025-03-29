'''
Created on Mar 25, 2025

@author: Aayush
'''
from request_server import constants
from request_server import crypto_metrics
import uuid

class RequestHandler:
    def __init__(self):
        self._cryptos = {}
        self._users = {
            "user": "pass123"
        }
        self._tokens = {}
    
    def _getCurrBtcPrice(self):
        currPrice = crypto_metrics.getCurrBtcPrice()
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS, 
                "Price" : currPrice}
    
    def _getBtcPriceHistory(self):
        history = crypto_metrics.getHistoricalData("1y")
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS,
                "History" : history}
        
    def handleSignUp(self, request):
        username = request.get(constants.KEY_USERNAME)
        password = request.get(constants.KEY_PASSWORD)
        confirmPassword = request.get(constants.KEY_CPASSWORD)

        if not username or not password or not confirmPassword:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Username, password, and confirmPassword are required"
            }
            
        if password != confirmPassword:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Passwords do not match"
            }

        if username in self._users:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Username already exists"
            }

        self._users[username] = password
        token = str(uuid.uuid4())
        self._tokens[token] = username

        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, constants.KEY_TOKEN: token}
    
    def handleLogin(self, request):
        username = request.get(constants.KEY_USERNAME)
        password = request.get(constants.KEY_PASSWORD)

        if not username or not password:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Username and password are required"
            }

        if username not in self._users or self._users[username] != password:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Invalid credentials"
            }
            
        token = str(uuid.uuid4())
        self._tokens[token] = username

        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, constants.KEY_TOKEN: token}
    
    def handleRequest(self, request):
        response = {constants.KEY_STATUS: constants.UNSUPPORTED_OPERATION_STATUS,
                   constants.KEY_FAILURE_MESSAGE: "Unsupported request type"}
        
        request_type = request.get(constants.KEY_REQUEST_TYPE, None)
        
        if(request_type == constants.GET_BTC_CURR_PRICE):
            response = self._getCurrBtcPrice()
        elif(request_type == constants.GET_BTC_PRICE_HISTORY):
            response = self._getBtcPriceHistory()
        elif(request_type == constants.GET_SIGN_UP):
            response = self.handleSignUp(request)
        elif(request_type == constants.GET_LOGIN):
            response = self.handleLogin(request)
            
        return response
