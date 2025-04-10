'''
Created on Apr 9, 2025

@author: Colby
'''
import unittest
from model.Account import Account
from model.Holding import Holding
class testAccount(unittest.TestCase):
    def setUp(self):
        self.account = Account("colby", "pw")
    def testConstructor(self):
        self.assertEqual(self.account.username, "colby")
        self.assertTrue(self.account.verify_password("pw"))
        self.assertEqual(self.account.funds_available, 0.00)
        self.assertTrue(not self.account.holdings)
    def testVerifyValidPassword(self):
        self.assertTrue(self.account.verify_password("pw"))
    def testVerifyInvaliddPassword(self):
        self.assertFalse(self.account.verify_password("differentPassword"))    
    def testAddNewHolding(self):
        holding = Holding("bitcoin", 1)
        self.account.add_holding(holding)
        self.assertFalse(not self.account.holdings)
        self.assertTrue(holding in self.account.holdings)
    def testAddMoreOfSameHolding(self):
        holding = Holding("bitcoin", 1)
        self.account.add_holding(holding)
        self.account.add_holding(holding)
        bitcoinHolding = self.account.get_holding("bitcoin")
        self.assertTrue(bitcoinHolding.amount_held == 2)
    def testEqualAccount(self):
        equalAccount = Account("colby", "pw")
        self.assertTrue(equalAccount == self.account)
    def testEmptyUserName(self):
        self.assertRaises(ValueError, Account, "", "pw")
    def testNotEqualAccount(self):
        uniqueAccount = Account("sam", "password")
        self.assertFalse(self.account == uniqueAccount)
        
if (__name__ == "__main__"):
    unittest.main()     