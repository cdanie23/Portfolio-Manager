'''
Created on Apr 3, 2025

@author: Colby
'''
class Holding:
   
    def __init__(self, name, amount):
        self.amount = amount
        self.name = name
        
    def set_amount_held(self, amount: float):
        self.amount = amount
        
    def getHoldingName(self):
        return self.name
    
    def getAmountHeld(self):
        return self.amount
        
    def to_dict(self):
        return {"name": self.name, "amount": self.amount}
