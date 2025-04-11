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

    def add_holding(self, holding: Holding) -> bool:
        """Adds or updates a holding for the user."""
        for curr_holding in self.holdings:
            if curr_holding.name == holding.name:
                curr_holding.amount += float(curr_holding.amount)
                return True

        self.holdings.append(holding)
        return True
    def get_holding(self, name):
        for holding in self.holdings:
            if (holding.name == name):
                return holding
    def __eq__(self, other):
        if isinstance(other, Account):
            return self.username == other.username