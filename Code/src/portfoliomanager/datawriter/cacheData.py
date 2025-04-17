'''
Created on Apr 16, 2025

@author: Colby
'''
import json
class CacheData:
    def __init__(self, fileName):
        '''
        Creates an instance of the CacheData class
        @post self.fileName == fileName
        '''
        self.fileName = fileName
        
    def cacheData(self, data):
        '''
        Writes the data as a json the file name
        '''
        with open(self.fileName, 'a') as f:
            json.dump(data, f)
           
    