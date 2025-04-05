import yfinance

bitcoin = yfinance.Ticker("BTC-USD")
def getCurrBtcPrice():
    '''
    Gets the price from the previous days closing price
    @return the price at the end of the previous day
    '''
    info = bitcoin.fast_info
    return info["lastPrice"]
    # info = bitcoin.info
    # current_price = info.get("regularMarketPrice", None)
    #
    # if current_price is None:
    #     raise ValueError("Could not get current price.")
    
    #return current_price


def getHistoricalData(timespan):
    history = bitcoin.history(period=timespan, interval="1d")[["Close"]]

    price_dict_str_dates = history["Close"].rename_axis("Date").reset_index()
    price_dict_str_dates["Date"] = price_dict_str_dates["Date"].dt.strftime("%Y-%m-%d")
    
    price_dict = dict(zip(price_dict_str_dates["Date"], price_dict_str_dates["Close"]))
    
    return price_dict

if (__name__=="__main__"):
    print(getHistoricalData("1y"))