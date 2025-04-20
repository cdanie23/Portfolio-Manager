'''
Created on Mar 25, 2025

@author: Aayush
'''
from request_server import constants
import uuid
from model.Account import Account
from model.Holding import Holding
from request_server.crypto_metrics import cache_dir_crypto_metrics

class RequestHandler:
    def __init__(self, trend):
        self.trend = trend
        account = Account("user", "pass123")
        holding = Holding("Bitcoin", 2.0)
        account.add_holding(holding)
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
        for account in self._users:
            if (username == account.username):
                response = {
                    constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                    constants.KEY_FAILURE_MESSAGE: "Username already exists"
                }
                return response
        account = Account(username, password)
        self._users.append(account)
        token = str(uuid.uuid4())
        self._tokens[token] = account
        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, constants.KEY_TOKEN: token}
    
    def handleAddHolding(self, request):
        auth = request[constants.KEY_TOKEN]
        amount = request[constants.KEY_AMOUNT]
        cryptoName = request[constants.KEY_NAME]
        account = self.findAccountByAuth(auth)
        if (not account or auth == None or amount == None or cryptoName == None):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "Not a valid request"
                }
            return response
        holding = Holding(cryptoName, float(amount))
        account.add_holding(holding)
        response = {
            constants.KEY_STATUS : constants.SUCCESS_STATUS,
            constants.KEY_TOKEN : auth
            }
        return response
    
    def findAccountByAuth(self, auth):
        account = None
        for token in self._tokens:
            if(token == auth):
                account = self._tokens[token]
                break;
        return account
               
    def findAccountByName(self, username, password):
        for account in self._users:
            if (account == Account(username, password)):
                return account
            
    def handleAddFunds(self, request):
        auth = request[constants.KEY_TOKEN]
        amount = request[constants.KEY_AMOUNT]
        account = self.findAccountByAuth(auth)
        if (not account or amount == None or auth == None):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "account with token not found"
                }
            return response
        
        account.funds_available += float(amount)
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS, 
                constants.KEY_TOKEN : auth,
                constants.KEY_FUNDS : account.funds_available}
        
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
        self._tokens[token] = self.findAccountByName(username, password)
        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, constants.KEY_TOKEN: token}
    

    def handleGetFunds(self, request):
        auth = request[constants.KEY_TOKEN]
        account = self.findAccountByAuth(auth)
        if (not account or auth == None):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "account with token not found"
                }
            return response
        fundsAvailable = account.funds_available
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS,
                constants.KEY_TOKEN : auth, 
                constants.KEY_AMOUNT : fundsAvailable}
    
    def handleGetHoldings(self, request):
        auth = request[constants.KEY_TOKEN]
        account = self.findAccountByAuth(auth)
        if (not account or auth == None):
            response = {
                constants.KEY_STATUS : constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE : "account with token not found"
                }
            return response
        holdings = account.holdings
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS,
                constants.KEY_TOKEN : auth, 
                constants.KEY_HOLDINGS : [h.to_dict() for h in holdings]}
        

    def handleLogout(self, request):
        token = request.get(constants.KEY_TOKEN)
        
        if not token or token not in self._tokens:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "Invalid or missing token"
            }
        
        del self._tokens[token]

        return {constants.KEY_STATUS: constants.SUCCESS_STATUS, "message": "Logout successful"}
    
    def handleGetAllCryptoData(self, filepath=cache_dir_crypto_metrics):
        crypto_metrics = self.trend.getCurrTrend()
        cryptoData = crypto_metrics.getHistoricalDataForAllCoins(filepath)
        if (cryptoData == None):
            return { 
                    constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, 
                    constants.KEY_FAILURE_MESSAGE : "Empty crypto data"
                    }
        return { 
                constants.KEY_STATUS: constants.SUCCESS_STATUS, 
                constants.KEY_CRYPTO_DATA: cryptoData
                }
        
    def handlePriceRequest(self, request):
        name = request[constants.GET_CRYPTO_NAME]
        if (name == None):
            return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, 
            constants.KEY_FAILURE_MESSAGE : "Empty crypto data"
            }
        price = self.trend.getCurrTrend().getCurrCryptoPrice(name)
        return {
            constants.KEY_STATUS: constants.SUCCESS_STATUS, 
            constants.KEY_CRYPTO_PRICE: float(price)
            }
    
    def handleRequest(self, request):
        response = {constants.KEY_STATUS: constants.UNSUPPORTED_OPERATION_STATUS,
                   constants.KEY_FAILURE_MESSAGE: "Unsupported request type"}
        
        request_type = request.get(constants.KEY_REQUEST_TYPE, None)
        
        if(request_type == constants.GET_SIGN_UP):
            response = self.handleSignUp(request)
        elif(request_type == constants.GET_LOGIN):
            response = self.handleLogin(request)
        elif(request_type == constants.ADD_HOLDING):
            response = self.handleAddHolding(request)
        elif(request_type == constants.ADD_FUNDS):
            response = self.handleAddFunds(request)
        elif(request_type == constants.GET_FUNDS):
            response = self.handleGetFunds(request)
        elif(request_type == constants.GET_HOLDINGS):
            response = self.handleGetHoldings(request)
        elif(request_type == constants.GET_LOGOUT):
            response = self.handleLogout(request)
        elif(request_type == constants.GET_CRYPTO_PRICE):
            response = self.handlePriceRequest(request)
        elif(request_type == constants.GET_CRYPTO_DATA):
            response = self.handleGetAllCryptoData()
            
        return response
