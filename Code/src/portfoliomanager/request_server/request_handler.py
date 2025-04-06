'''
Created on Mar 25, 2025

@author: Aayush
'''
from request_server import constants
from request_server import crypto_metrics
import uuid
from model.Account import Account
from model.Holding import Holding
class RequestHandler:
    def __init__(self):
        account = Account("user", "pass123")
        self._cryptos = {}
        self._users = [account]
        self._tokens = {"$123" : account}
        
    def makeAccount(self, username, password):
        account = Account(username, password)
        self._users.append(account)
    def authenticate_user(self, username, password):
        for account in self._users:
            if account.username == username and account.verify_password(password):
                return True  
        return False  
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
        account = Account(username, password)
        self._users.append(account)
        token = str(uuid.uuid4())
        self._tokens[token] = account

        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, constants.KEY_TOKEN: token}
    def handleAddHolding(self, request):
        auth = request[constants.KEY_TOKEN]
        amount = request[constants.KEY_AMOUNT]
        cryptoName = request[constants.KEY_NAME]
        
        account = self.findAccount(auth)
        if (not account):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "account with token not found"
                }
            return response
        holding = Holding(cryptoName, amount)
        account.add_holding(holding)
        response = {
            constants.KEY_STATUS : constants.SUCCESS_STATUS,
            constants.KEY_TOKEN : auth
            }
        return response
    def findAccount(self, auth):
        for token in self._tokens:
            if(token == auth):
                account = self._tokens[token]
                break
        return account
    def handleAddFunds(self, request):
        auth = request[constants.KEY_TOKEN]
        amount = request[constants.KEY_AMOUNT]
        account = self.findAccount(auth)
        if (not account):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "account with token not found"
                }
            return response
        account.funds_available += float(amount)
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS, 
                constants.KEY_TOKEN : auth}
    def handleLogin(self, request):
        username = request.get(constants.KEY_USERNAME)
        password = request.get(constants.KEY_PASSWORD)

        if not username or not password:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Username and password are required"
            }

        if not self.authenticate_user(username, password):
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
        elif(request_type == constants.ADD_HOLDING):
            response = self.handleAddHolding(request)
        elif(request_type == constants.ADD_FUNDS):
            response = self.handleAddFunds(request)
        return response
