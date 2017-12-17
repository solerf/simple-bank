CREATE TABLE TB_CLIENT (
  ID INTEGER IDENTITY PRIMARY KEY,
  EMAIL VARCHAR(50) NOT NULL,
  NAME VARCHAR(50) NOT NULL,
  PASSWORD VARCHAR(50) NOT NULL
);

CREATE TABLE TB_ACCOUNT (
  ID INTEGER IDENTITY PRIMARY KEY,
  ACCT_NUMBER INTEGER NOT NULL,
  CLIENT_ID INTEGER NOT NULL
);
ALTER TABLE TB_ACCOUNT ADD CONSTRAINT FK_CLIENT__ACCOUNT FOREIGN KEY (CLIENT_ID) REFERENCES TB_CLIENT;

CREATE TABLE TB_ACCT_MOVEMENTS (
  ID INTEGER IDENTITY PRIMARY KEY,
  AMOUNT DOUBLE NOT NULL,
  OPERATION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  TYPE VARCHAR(50) NOT NULL,
  ACCOUNT_ID INTEGER NOT NULL,
  CLIENT_ID INTEGER NOT NULL
);
ALTER TABLE TB_ACCT_MOVEMENTS ADD CONSTRAINT FK_ACCOUNT_ACCT_MOVEMENT FOREIGN KEY (ACCOUNT_ID) REFERENCES TB_ACCOUNT;
ALTER TABLE TB_ACCT_MOVEMENTS ADD CONSTRAINT FK_CLIENT_ACCT_MOVEMENT FOREIGN KEY (CLIENT_ID) REFERENCES TB_CLIENT;
