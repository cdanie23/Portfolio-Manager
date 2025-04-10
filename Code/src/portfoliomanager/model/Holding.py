'''
Created on Apr 3, 2025

@author: Colby
'''
class Holding:
   
    def __init__(self, name, amount):
        self.amount_held = amount
        self.name = name
        
    def set_amount_held(self, amount: float):
        self.amount_held = amount
        
    def getHoldingName(self):
        return self.name
    
    def getAmountHeld(self):
        return self.amount_held
        

