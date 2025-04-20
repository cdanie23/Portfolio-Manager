'''
Created on Apr 18, 2025

@author: aayush
'''
import unittest
from unittest.mock import patch, mock_open
from datetime import datetime
import json
from datawriter.cacheData import CacheData

class TestCacheData(unittest.TestCase):

    @patch('datareader.readCache.ReadCache') 
    @patch('builtins.open', new_callable=mock_open)
    @patch('datetime.datetime')
    def testAddNewData(self, mock_datetime, mock_open, MockReadCache):
        mock_cache_data = {
            'Bitcoin': {'01/01/25': 25000},
            'Ethereum': {'01/01/25': 1800}
        }
        
        MockReadCache.return_value.readCache.return_value = mock_cache_data
        
        curr_trend = [
            {'name': 'Bitcoin', 'current_price': 25500},
            {'name': 'Ethereum', 'current_price': 1850}
        ]
        mock_open.return_value.read.return_value = json.dumps(mock_cache_data)
        
        cache_data = CacheData('mock_cache.json')
        
       
        cache_data.addNewData(curr_trend)
        
        mock_open.assert_any_call('mock_cache.json', 'r')
        
        mock_open.assert_any_call('mock_cache.json', 'w')
        written_data = ''.join(call[0][0] for call in mock_open().write.call_args_list)
        written_data_json = json.loads(written_data)
        
        today_date = datetime.today().strftime('%d/%m/%y')
        self.assertEqual(written_data_json['Bitcoin'][today_date], 25500)
        self.assertEqual(written_data_json['Ethereum'][today_date], 1850)
        self.assertEqual(written_data_json['Bitcoin']['01/01/25'], 25000)
        self.assertEqual(written_data_json['Ethereum']['01/01/25'], 1800)


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()