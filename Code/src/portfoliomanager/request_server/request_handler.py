'''
Created on Mar 25, 2025

@author: Aayush
'''
from request_server import constants

class RequestHandler:
    def __init__(self):
        self._cryptos = {}
        
    def _getCryptos(self,request):
        return {constants.KEY_STATUS: constants.SUCCESS_STATUS}
    
    def handleRequest(self, request):
        response = {constants.KEY_STATUS: constants.UNSUPPORTED_OPERATION_STATUS,
                   constants.KEY_FAILURE_MESSAGE: "Unsupported request type"}
        
        request_type = request.get(constants.KEY_REQUEST_TYPE, None)
        
        if request_type == constants.GET_CRYPTOS:
            response = self._getCryptos(request)
        
        return response