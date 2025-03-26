'''
Created on Mar 25, 2025

@author: Aayush
'''
import zmq
import time

def runServer():
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind("tcp://127.0.0.1:5555")
    
    while True:
        #  Wait for next request from client
        print("waiting for message...")
        message = socket.recv()
        print("Received request: %s" % message)
        if(message == b"exit"):
            return
        #  Do some 'work'
        time.sleep(1)
    
        #  Send reply back to client
        socket.send(b"World")
        
if(__name__ == "__main__"):
    runServer()