from pycoingecko import CoinGeckoAPI
from datetime import datetime
import os
from pathlib import Path
import threading
from datawriter.cacheData import CacheData
from datareader.readCache import ReadCache
cg = CoinGeckoAPI()

current_file = Path(__file__).resolve()
cache_dir_crypto_metrics = current_file.parent.parent.parent.parent / "cache" / "cryptometrics.txt"

class CryptoMetric():
    
    def __init__(self, curr_trend = None):
        if curr_trend is not None:
            self.curr_trend = curr_trend
        else:
            self.curr_trend = cg.get_coins_markets("usd")
            
    def getCurrCryptoPrice(self, crypto_id):
        '''
        Gets the price from the previous days closing price
        @return the price at the end of the previous day
        '''
        for curr_crypto in self.curr_trend:
            if (curr_crypto.get('name').upper()== crypto_id.upper()):
                price = curr_crypto.get('current_price')
        return price
    
    def getHistoricalData(self, coin_id):
        '''
        Gets the historical data of bitcoin for one year
        @param id - the id of the coin you want to get the data for
        @returns a dictionary of date and price <k,v> pairs
        '''
        
        info = cg.get_coin_market_chart_by_id(coin_id, "usd", "365")
        prices = info["prices"]
        
        for i in range(len(prices)):
            pricesArray = prices[i]
            timeInSecs = pricesArray[0] / 1000
            pricesArray[0] = datetime.fromtimestamp(timeInSecs).strftime('%d/%m/%y')
        
        price_dict = {date: price for date, price in prices}
        return price_dict
    
    def getHistoricalDataForAllCoins(self, filepath=cache_dir_crypto_metrics):
        '''
        Gets the historical data for all cryptocurrencies
        @returns a dictionary of dictionaries where the key is the name of the bitcoin and the dictionary
        returned has a key of dates and value of the price of that day
        '''
        
        if (os.path.exists(filepath)):
            readCache = ReadCache(filepath)
            return readCache.readCache()
        else:
            data = {}
            for i in range(10):
                coinData = self.curr_trend[i]
                coinId = coinData["id"]
                coinName = coinData["name"]
                historicalData = self.getHistoricalData(coinId)
                data[coinName] = historicalData
            cacheData = CacheData(None)
            cacheData.cacheData(data)
            return data
            
        cacheData = CacheData(cache_dir_crypto_metrics)
        new_data_thread = threading.Thread(target=cacheData.addNewData(self.curr_trend))
        new_data_thread.daemon = True
        new_data_thread.start()
        return None
