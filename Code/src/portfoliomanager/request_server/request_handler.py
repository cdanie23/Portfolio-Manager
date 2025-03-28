'''
Created on Mar 25, 2025

@author: Aayush
'''
from request_server import constants
from request_server import crypto_metrics

class RequestHandler:
    def __init__(self):
        self._cryptos = {}
        
    def _getCryptos(self,request):
        return {constants.KEY_STATUS: constants.SUCCESS_STATUS}
    
    def _getCurrBtcPrice(self):
        currPrice = crypto_metrics.getCurrBtcPrice()
        return {constants.KEY_STATUS : constants.SUCCESS_STATUS, 
                "Price" : currPrice}
    
    def handleRequest(self, request):
        response = {constants.KEY_STATUS: constants.UNSUPPORTED_OPERATION_STATUS,
                   constants.KEY_FAILURE_MESSAGE: "Unsupported request type"}
        
        request_type = request.get(constants.KEY_REQUEST_TYPE, None)
        
        if request_type == constants.GET_CRYPTOS:
            response = self._getCryptos(request)
        if request_type == constants.GET_BTC_CURR_PRICE:
            response = self._getCurrBtcPrice()
        return response
  
  

    
        