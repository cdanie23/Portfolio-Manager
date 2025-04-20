'''
Created on Apr 20, 2025

@author: aayush
'''
import unittest
from unittest.mock import patch
from request_server.trend_holder import TrendHolder


class TestTrendHolder(unittest.TestCase):

    @patch("request_server.trend_holder.CryptoMetric")
    def testCreateTrendHolder(self, mock_crypto_metric):
        holder = TrendHolder()
        self.assertTrue(holder.getCurrTrend())
        mock_crypto_metric.assert_called_once()
    
    @patch("request_server.trend_holder.CryptoMetric")
    @patch("request_server.trend_holder.cg.get_coins_markets")
    def testUpdateCurrTrend(self, mock_get_markets, mock_crypto_metric):
        mock_data = [{"id": "bitcoin", "name": "Bitcoin", "current_price": 60000}]
        mock_get_markets.return_value = mock_data
        holder = TrendHolder()
        holder.updateTrend()
        mock_get_markets.assert_called_once_with("usd")
        mock_crypto_metric.assert_called_with(curr_trend=mock_data)

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testTrendHolder']
    unittest.main()