'''
Created on Apr 9, 2025

@author: Aayush
'''
import unittest
from model.Holding import Holding
from model.Account import Account

class TestAccount(unittest.TestCase):
    
    def setUp(self):
        self._username = "test_user"
        self._password = "test_password"
        self._account = Account(self._username, self._password)
    
    def testConstructor(self):
        self.assertEqual(self._account.username, self._username)
        self.assertTrue(self._account.verify_password(self._password))
        
    def testInvalidAccountCreation(self):
        with self.assertRaises(ValueError):
            Account("", self._password)
            
        with self.assertRaises(ValueError):
            Account(self._username, "")
            
    def testSetUsername(self):
        new_username = "new_user"
        self._account.username = new_username
        self.assertEqual(self._account.username, new_username)
        
            
    def testChangePassword(self):
        old_password = self._password
        new_password = "new_password"
        
        self._account.change_password(old_password, new_password)
        self.assertFalse(self._account.verify_password(old_password))
        
        with self.assertRaises(ValueError):
            self._account.change_password(old_password, "")
            
        with self.assertRaises(ValueError):
            self._account.change_password("wrongpassword", new_password)
        
        with self.assertRaises(ValueError):
            self._account.change_password(old_password, "")
            
    def testAddHolding(self):
        holding1 = Holding("BTC", 1.5)
        holding2 = Holding("ETH", 2.0)
        
        self._account.add_holding(holding1)
        self._account.add_holding(holding2)
        
        holdings = self._account.holdings
        self.assertEqual(len(holdings), 2)
        self.assertEqual(holdings[0].name, "BTC")
        self.assertEqual(holdings[1].name, "ETH")
        
        holding3 = Holding("BTC", 0.5)
        self._account.add_holding(holding3)
        
        holdings = self._account.holdings
        self.assertEqual(holdings[0].amount, 3.0)
        
    
    def testGettersAndSSettersForFunds(self):
        self._account.funds_available = 1000.0
        self.assertEqual(self._account.funds_available, 1000.0)
        
    def testAccountEquality(self):
        another_account = Account(self._username, self._password)
        self.assertEqual(self._account, another_account)
        different_account = Account("different_user", self._password)
        self.assertNotEqual(self._account, different_account)
        
  
        
    if __name__ == "__main__":
        unittest.main()
    