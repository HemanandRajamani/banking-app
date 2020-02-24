Run the main method to start the  application.

API's:

1. Create an account:
 POST - http://localhost:4567/api/v1.0/account
 
2. To deposit money
PUT - http://localhost:4567/api/v1.0/account/1000/deposit/20.00

3. To withdraw money
PUT - http://localhost:4567/api/v1.0/account/1000/withdraw/10.00

4. To transfer money between accounts
PUT - http://localhost:4567/api/v1.0/account/1000/transfer
        
        {
           "amount": "15",
           "toAccount": "1001"
        }
5. Delete an account
DELETE - http://localhost:4567/api/v1.0/account/1000
