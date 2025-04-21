'''
Created on Apr 19, 2025

@author: Aayush
'''
import unittest
from unittest.mock import patch
from request_server.crypto_metrics import CryptoMetric


class Test(unittest.TestCase):

    @patch('request_server.crypto_metrics.cg.get_coins_markets')
    def testGetCurrCryptoPriceCoinGecko(self, mock_get_coins_markets):
        mock_get_coins_markets.return_value = [
            {'name': 'Bitcoin', 'current_price': 60000},
            {'name': 'Ethereum', 'current_price': 3000}
        ]

        crypto_metric = CryptoMetric()
        
        price = crypto_metric.getCurrCryptoPrice('Bitcoin')
        self.assertEqual(price, 60000)

        price = crypto_metric.getCurrCryptoPrice('Ethereum')
        self.assertEqual(price, 3000)

    @patch('request_server.crypto_metrics.cg.get_coins_markets')
    def testGetCurrCryptoPrice(self, mock_get_coins_markets):
        mock_curr_trend = [
            {'name': 'Bitcoin', 'current_price': 45000},
            {'name': 'Ethereum', 'current_price': 2200}
        ]

        crypto_metric = CryptoMetric(curr_trend=mock_curr_trend)

        price = crypto_metric.getCurrCryptoPrice('Bitcoin')
        self.assertEqual(price, 45000)

        price = crypto_metric.getCurrCryptoPrice('Ethereum')
        self.assertEqual(price, 2200)

    @patch('request_server.crypto_metrics.cg.get_coin_market_chart_by_id')
    def testGetHistoricalData(self, mock_get_coin_market_chart_by_id):
        mock_get_coin_market_chart_by_id.return_value = {
            'prices': [
                [1632769200000, 50000],  
                [1632855600000, 51000]
            ]
        }

        crypto_metric = CryptoMetric()

        historical_data = crypto_metric.getHistoricalData('bitcoin')
        self.assertEqual(historical_data['27/09/21'], 50000)
        self.assertEqual(historical_data['28/09/21'], 51000)

    @patch('datawriter.cacheData.CacheData.addNewData')
    @patch('datareader.readCache.ReadCache.readCache')
    def testGetHistoricalDataForAllCoins(self, mock_read_cache, mock_add_new_data):
        mock_read_cache.return_value = {
            'Bitcoin': {'29/09/21': 50000, '30/09/21': 51000},
            'Ethereum': {'29/09/21': 3000, '30/09/21': 3200}
        }
        crypto_metric = CryptoMetric()
        historical_data = crypto_metric.getHistoricalDataForAllCoins()
        
        self.assertIn('Bitcoin', historical_data)
        self.assertIn('Ethereum', historical_data)
        self.assertEqual(historical_data['Bitcoin']['29/09/21'], 50000)
        self.assertEqual(historical_data['Ethereum']['29/09/21'], 3000)
        
    def testGetHistoricalDataForAllCoinsNone(self):
        crypto_metric = CryptoMetric(curr_trend = None)
        historical_data = crypto_metric.getHistoricalDataForAllCoins("abc")
        self.assertIsNone(historical_data)


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()