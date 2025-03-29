import yfinance

bitcoin = yfinance.Ticker("BTC-USD")
def getCurrBtcPrice():
    '''
    Gets the price from the previous days closing price
    @return the price at the end of the previous day
    '''
    info = bitcoin.fast_info
    return info["lastPrice"]


def getHistoricalData(timespan):
    history = bitcoin.history(period=timespan, interval="1d")
    priceColumn = history["Close"]
    priceDict = priceColumn.to_dict()
    priceDictStrDates = {date.strftime("%Y-%m-%d"): price 
    for date, price in priceDict.items()}
    return priceDictStrDates
