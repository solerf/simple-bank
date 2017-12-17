# SIMPLE BANK
##### Created by Felipe Soler, 2017

### Running the application
- open cmd
- cd to the dir where simple-bank.jar is located
- in cmd execute 

```
java -jar simple-bank.jar
```

### API endpoints
 - /login
    - POST
    - Parameters: email:String, password:String
    - Clients login
 - /account/{acctNumber}
    - GET
    - Parameters: token:String, acctNumber:Integer
    - Withdraw desired amount from one of client's account
 - /account/{acctNumber}/deposit
    - POST
    - Parameters: token:String, acctNumber:Integer, amount:Double
    - Deposit desired amount into specific account
 - /account/{acctNumber}/withdraw
    - POST
    - Parameters: token:String, acctNumber:Integer, amount:Double
    - Get account details like current balance and statement

### Assumptions
- client can only have one account
- client can do deposits in different accounts, not only the ones he owns
- no operations for creation of clients or accounts, assumed that they are created by other system