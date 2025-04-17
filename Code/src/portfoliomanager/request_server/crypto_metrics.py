from pycoingecko import CoinGeckoAPI
from datetime import datetime
import os
from pathlib import Path


from datareader.readCache import ReadCache
cg = CoinGeckoAPI()


current_file = Path(__file__).resolve()
cache_dir_crypto_metrics = current_file.parent.parent.parent.parent / "cache" / "cryptometrics.txt"

def getCurrBtcPrice():
    '''
    Gets the price from the previous days closing price
    @return the price at the end of the previous day
    '''
    info = cg.get_price(ids="bitcoin", vs_currencies="usd", precision="5")
    price = info["bitcoin"]["usd"]
    
    return price
    
    # info = bitcoin.info
    # current_price = info.get("regularMarketPrice", None)
    #
    # if current_price is None:
    #     raise ValueError("Could not get current price.")
    
    #return current_price

def getHistoricalData(id):
    '''
    Gets the historical data of bitcoin for one year
    @param id - the id of the coin you want to get the data for
    @returns a dictionary of date and price <k,v> pairs
    '''
    
    info = cg.get_coin_market_chart_by_id(id, "usd", "365")
    prices = info["prices"]
    
    for i in range(len(prices)):
        pricesArray = prices[i]
        timeInSecs = pricesArray[0] / 1000
        pricesArray[0] = datetime.fromtimestamp(timeInSecs).strftime('%d/%m/%y')
    
    price_dict = {date: price for date, price in prices}
    


    return price_dict
def getHistoricalDataForAllCoins(filepath=cache_dir_crypto_metrics):
    '''
    Gets the historical data for all cryptocurrencies
    @returns a dictionary of dictionaries where the key is the name of the bitcoin and the dictionary
    returned has a key of dates and value of the price of that day
    '''
    
    if (os.path.exists(filepath)):
        readCache = ReadCache(filepath)
        return readCache.readCache()
    return None
    
if (__name__ == "__main__"):
    print(getHistoricalDataForAllCoins())
