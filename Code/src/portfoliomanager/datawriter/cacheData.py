'''
Created on Apr 16, 2025

@author: Colby
'''
import json
from datetime import datetime
from datareader.readCache import ReadCache
from pathlib import Path
import os 
current_file = Path(__file__).resolve()
cache_dir_crypto_metrics = current_file.parent.parent.parent.parent / "cache" 
class CacheData:
    def __init__(self, fileName):
        '''
        Creates an instance of the CacheData class
        @post self.fileName == fileName
        '''
    
        if (fileName == None):
            fileName = "cryptometrics.txt"
            filePath = os.path.join(cache_dir_crypto_metrics, fileName)
            fileName = filePath
            
        self.fileName = fileName
        
    def cacheData(self, data):
        '''
        Writes the data as a json the file name
        '''
        with open(self.fileName, 'w') as f:
            json.dump(data, f)
           
    def addNewData(self, curr_trend):
        readCache = ReadCache(self.fileName)
        today_date = datetime.now().strftime("%d/%m/%y")
        updated_crypto_data = {}
        for crypto_name in readCache.readCache():
            currCryptoData = readCache.readCache().get(crypto_name)
            if today_date not in currCryptoData:
                currCryptoData[today_date] = self.todaysCryptoPrice(crypto_name, curr_trend)
            updated_crypto_data[crypto_name] = currCryptoData
            
        self.cacheData(updated_crypto_data)
        
    def todaysCryptoPrice(self, crypto_name, curr_trend):
        '''
        Gets the price from the previous days closing price
        @return the price at the end of the previous day
        '''
        for curr_crypto in curr_trend:
            if (curr_crypto.get('name').upper() == crypto_name.upper()):
                price = curr_crypto.get('current_price')
        return price
if (__name__ == "__main__"):
    cacheData = CacheData(None)
    cacheData.cacheData("hello world")
            