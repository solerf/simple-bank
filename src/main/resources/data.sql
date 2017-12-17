-- clients
insert into tb_client(id, name, email, password)
values(1, 'tony', 'tony@email.com', 'tony123');

insert into tb_client(id, name, email, password)
values(2, 'carlos', 'carlos_account@myemail.com', 'my_password');

insert into tb_client(id, name, email, password)
values(3, 'mary', 'mary@marycompany.com', 'marymary');

-- accounts
insert into tb_account(id, acct_number, client_id)
values(1, 54540, 1);

insert into tb_account(id, acct_number, client_id)
values(2, 54541, 1);

insert into tb_account(id, acct_number, client_id)
values(3, 69000, 2);

insert into tb_account(id, acct_number, client_id)
values(4, 70001, 3);

insert into tb_account(id, acct_number, client_id)
values(5, 70011, 3);