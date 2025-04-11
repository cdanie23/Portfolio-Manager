'''
Created on Apr 3, 2025

@author: Colby
'''
class Holding:
    '''
    Instantiates a holding
    @post self.amount == amount, self.name == name
    '''
    def __init__(self, name, amount):
        self.amount = amount
        self.name = name
        
    def set_amount_held(self, amount: float):
        '''
        Sets the amount held
        @post self.amount = amount
        '''
        self.amount = amount
        
    def getHoldingName(self):
        '''
        Gets the name of the holding
        @returns self.name
        '''
        return self.name
    
    def getAmountHeld(self):
        '''
        Gets the amount held
        @returns the amount held
        '''
        return self.amount
        
    def to_dict(self):
        '''
        transforms the contents of the holding to a dictionary
        @returns a dictionary formatted as follows
        '''
        return {"name": self.name, "amount": self.amount}
