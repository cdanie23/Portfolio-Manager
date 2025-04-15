from pycoingecko import CoinGeckoAPI
import datetime



cg = CoinGeckoAPI()
def getCurrBtcPrice():
    '''
    Gets the price from the previous days closing price
    @return the price at the end of the previous day
    '''
    info = cg.get_price(ids="bitcoin", vs_currencies="usd")
    price = info["bitcoin"]["usd"]
    return float(price)
    
    
    # info = bitcoin.info
    # current_price = info.get("regularMarketPrice", None)
    #
    # if current_price is None:
    #     raise ValueError("Could not get current price.")
    
    #return current_price


def getHistoricalData():
    '''
    Gets the historical data of bitcoin for one year
    @returns a dictionary of date and price <k,v> pairs
    '''
    info = cg.get_coin_market_chart_by_id("bitcoin", "usd", "365")
    prices = info["prices"]
    
    for i in range(len(prices)):
        pricesArray = prices[i]
        timeInSecs = pricesArray[0] / 1000
        pricesArray[0] = datetime.datetime.fromtimestamp(timeInSecs).strftime('%d/%m/%y')
    
    price_dict = {date: price for date, price in prices}

    return price_dict

if (__name__ == "__main__"):
    print(getCurrBtcPrice())
