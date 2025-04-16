'''
Created on Apr 3, 2025

@author: Colby
'''
from model import Holding
import bcrypt
'''
The account class
'''
class Account:
    '''
    Instanties an instance of an account
    @pre username and password is not empty
    @post self.username == username, self.password_hash == random hashed value
          self.holdings is empty, self.funds_available == 0
    '''
    def __init__(self, username, password):
        if not username.strip() or not password.strip():
            raise ValueError("Username or password don't meet specified requirements.")

        self.username = username
        self.password_hash = self.hash_password(password)
        self.holdings = []
        self.funds_available = 0.0
    
    def hash_password(self, password: str) -> bytes:
        """Hashes a password using bcrypt."""
        return bcrypt.hashpw(password.encode(), bcrypt.gensalt())

    def verify_password(self, password: str) -> bool:
        """Verifies the provided password against the stored hash."""
        return bcrypt.checkpw(password.encode(), self.password_hash)

    def modify_holding(self, holding: Holding, add=True) -> bool:
        '''
        Adds a holding to the account
        @post holding is in holdings
        '''
        for curr_holding in self.holdings:
            if curr_holding.name == holding.name:
                if(add):
                    curr_holding.amount += float(holding.amount)
                else:
                    curr_holding.amount -= float(holding.amount)
                
                holding.set_amount_held(curr_holding.amount)
                return True
                

        self.holdings.append(holding)
        return True
    
    def modify_funds(self, totalCost: float, add= True):
        if (add):
            self.funds_available += float(totalCost)
        else:
            self.funds_available -= float(totalCost)
        
    def get_holding(self, name):
        '''
        Gets the holding in the account given the name
        @returns the holding with that name
        '''
        for holding in self.holdings:
            if (holding.name == name):
                return holding
    def change_password(self, current_password: str, new_password: str):
        """Changes the account password after verifying the current one."""
        if not new_password.strip():
            raise ValueError("New password must not be empty.")
        
        if not self.verify_password(current_password):
            raise ValueError("Current password is incorrect.")
        
        self.password_hash = self.hash_password(new_password)
    def __eq__(self, other):
        '''
        Overrides the == operator with the following'''
        if isinstance(other, Account):
            return self.username == other.username