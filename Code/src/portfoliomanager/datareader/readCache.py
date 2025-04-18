'''
Created on Apr 16, 2025

@author: Colby
'''
import json

class ReadCache:
    '''
    Creates an instance of the ReadCache class
    @post self.fileName == fileName
    '''
    def __init__(self, fileName):
        self.fileName = fileName
    
    
    '''
    Reads the cached data
    @returns the data formated as {"name of cryto" : {"date" : Price} }
    '''    
    def readCache(self):
        with open(self.fileName, "r") as f:
            data = json.load(f)  
        return data;
            