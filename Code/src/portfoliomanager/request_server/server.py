'''

Created on Mar 25, 2025

@author: Aayush
'''
import zmq
import json
from request_server import constants
from request_server.request_handler import RequestHandler

def log(message):
    print("Server::{0}".format(message))

def runServer(protocol, ip_address, port):
    request_handler = RequestHandler()
    
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind(f"{protocol}://{ip_address}:{port}")
    
    while True:
        
        log("waiting for message...")
        json_message = socket.recv_string()
        log(f"Received message: {json_message}")
        
        request = json.loads(json_message)
       
        
        requestType = request["type"]
        if(requestType == "exit"):
            log("Shutting down request_server")
            return
        elif constants.KEY_REQUEST_TYPE not in request:
            response = {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "No request type provided"}
            
            json_response = json.dumps(response)
            socket.send_string(json_response)
        else:
            response = request_handler.handleRequest(request)
            json_response = json.dumps(response)
            socket.send_string(json_response)
        
if(__name__ == "__main__"):
    runServer(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT)