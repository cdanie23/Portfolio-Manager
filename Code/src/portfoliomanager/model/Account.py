'''
Created on Apr 3, 2025

@author: Colby
'''
from model import Holding
import bcrypt
class Account:
    
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

    def set_username(self, new_name: str):
        if not new_name.strip():
            raise ValueError("Username doesn't meet specified requirements.")
        self.username = new_name

    def set_password(self, old_password: str, new_password: str):
        if not self.verify_password(old_password):
            raise ValueError("The old password is incorrect.")
        
        if not new_password.strip():
            raise ValueError("The new password doesn't meet requirements.")
        
        self.password_hash = self.hash_password(new_password)

    def get_holdings(self):
        return self.holdings

    def add_holding(self, holding: Holding) -> bool:
        """Adds or updates a holding for the user."""
        for curr_holding in self.holdings:
            if curr_holding.name == holding.name:
                curr_holding.set_amount_held(curr_holding.amount_held + holding.amount_held)
                return True

        self.holdings.append(holding)
        return True

    def get_funds_available(self) -> float:
        return self.funds_available

    def set_funds_available(self, amount: float):
        self.funds_available = amount