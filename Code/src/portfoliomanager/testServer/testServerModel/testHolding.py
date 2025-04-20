'''
Created on Apr 9, 2025

@author: Aayush
'''
import unittest
from model.Holding import Holding

class testHolding(unittest.TestCase):
    def setUp(self):
        self._amount_held = 5.0
        self._name = "BTC"
        self._holding = Holding(self._name, self._amount_held)
        
    def testConstructor(self):
        self.assertEqual(self._holding.getHoldingName(), self._name)
        self.assertEqual(self._amount_held, self._holding.getAmountHeld())
        
        
    def testSetAmountHeld(self):
        self._holding.set_amount_held(6);
        
        self.assertEqual(6, self._holding.getAmountHeld())