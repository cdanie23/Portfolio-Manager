'''
Created on Apr 20, 2025

@author: aayush
'''
import threading
from pycoingecko import CoinGeckoAPI
from request_server.crypto_metrics import CryptoMetric
cg = CoinGeckoAPI()

class TrendHolder:
    '''
    A wrapper class to hold trend data
    '''

    def __init__(self):
        '''
        Instantiates a new trendHolder
        '''
        self.curr_trend = CryptoMetric()
        self.lock = threading.Lock()
        
    def getCurrTrend(self):
        with self.lock:
            return self.curr_trend
        
    def updateTrend(self):
        new_data = cg.get_coins_markets("usd")
        with self.lock:
            self.curr_trend = CryptoMetric(curr_trend = new_data)