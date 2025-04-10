'''
Created on Mar 25, 2025

@author: Aayush
'''


PROTOCOL = "tcp"
IP_ADDRESS = "127.0.0.1"
PORT = "5555"

SUCCESS_STATUS = 1
BAD_MESSAGE_STATUS = -1
UNSUPPORTED_OPERATION_STATUS = -1

KEY_REQUEST_TYPE = "type"
KEY_FAILURE_MESSAGE = "error description"
KEY_STATUS = "success code"
EXIT = "exit"

GET_BTC_CURR_PRICE = "btcPrice"
GET_BTC_PRICE_HISTORY = "btcHistory"
GET_SIGN_UP = "signUp"
GET_LOGIN = "login"
ADD_HOLDING = "addHolding"
ADD_FUNDS = "addFunds"
GET_FUNDS = "getFunds"
GET_HOLDINGS = "getHoldings"
GET_LOGOUT = "logout"

# user keys
KEY_TOKEN = "token"
KEY_USERNAME = "username"
KEY_PASSWORD = "password"
KEY_CPASSWORD = "confirmPassword"
KEY_AMOUNT = "amount"
KEY_NAME = "name"
KEY_FUNDS = "funds"
KEY_HOLDINGS = "holdings"