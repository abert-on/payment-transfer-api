# payment-transfer-api

Coding exercise solution for Revolut. 

Program executable from Application.java.

Contains the following endpoints:
- GET /accounts
- GET /accounts/:accountUid
- PUT /accounts/:accountUid/transfer

### Comments
- ~~Tests doesn't pass, for some suites seems like @BeforeClass annotation doesn't work and the web service doesn't start~~
- ~~Concurrency is not handled - lock on balance which is a mutable property of the model - will not work~~
- Plain text responses for errors
- ~~Active-record style models (containing a link to the repository)~~
- ~~Dependency injection via static variables~~
