CREATE TABLE Person(id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(50) NOT NULL UNIQUE,
email varchar(50) not null unique);

CREATE TABLE Company (id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(50) NOT NULL);

CREATE TABLE Person_Company (person_id int REFERENCES person(id),
company_id int REFERENCES company(id));

CREATE TABLE Evention(uid uuid DEFAULT gen_random_uuid () PRIMARY key not null unique, company_id int REFERENCES company(id) NOT NULL,
name varchar(50) NOT NULL, start_time timestamp, end_time timestamp, balance NUMERIC(100,2);

CREATE TABLE Operation(uid uuid DEFAULT gen_random_uuid () PRIMARY key not null unique, evention_uid uuid REFERENCES evention(uid),
name varchar(50) NOT NULL, value NUMERIC(100,2), time timestamp, note varchar, person_id int REFERENCES person(id));

CREATE TABLE Paid_for(uid uuid DEFAULT gen_random_uuid () PRIMARY key not null unique, operation_uid uuid REFERENCES operation(uid),
value NUMERIC(100,2), person_id int REFERENCES person(id));

CREATE TABLE Person_Evention (person_id int REFERENCES person(id),
evention_id uuid REFERENCES evention(uid));

Проверки:
INSERT INTO person (name, email) VALUES ('Name1', 'Inception1');
INSERT INTO company (name) VALUES ('Company1');
INSERT INTO person (name, email) VALUES ('Name2', 'Inception2');
INSERT INTO person (name, email) VALUES ('Name3', 'Inception3');
DELETE FROM person  WHERE id=2;
INSERT INTO Evention (uid,company_id,"name") VALUES ('967b5d98-f1fa-4f6b-8ba5-4908efef6987', 1, 'name2');

INSERT INTO person_company (person_id, company_id) VALUES (32, 2);
INSERT INTO person_company (person_id, company_id) VALUES (31, 2);
INSERT INTO person_company (person_id, company_id) VALUES (29, 2);
INSERT INTO person_company (person_id, company_id) VALUES (30, 2);

INSERT INTO person_company (person_id, company_id) VALUES (32, 4);
INSERT INTO person_company (person_id, company_id) VALUES (31, 4);
INSERT INTO person_company (person_id, company_id) VALUES (29, 4);
INSERT INTO person_company (person_id, company_id) VALUES (30, 4);

INSERT INTO person_company (person_id, company_id) VALUES (32, 3);

drop table company;
drop table person;
drop table Person_Company;
drop table Evention;
drop table Operation;
drop table Payed_for;

TRUNCATE TABLE company  CASCADE;
TRUNCATE TABLE person  CASCADE;
SHOW logging_collector;
ALTER TABLE evention DROP COLUMN balance;
ALTER TABLE evention  add COLUMN  balance NUMERIC(100,2);
ALTER TABLE Operation DROP COLUMN value;
ALTER TABLE Operation  add COLUMN  value NUMERIC(100,2);
ALTER TABLE Payed_for DROP COLUMN value;
ALTER TABLE Payed_for  add COLUMN  value NUMERIC(100,2);
ALTER TABLE Payed_for  add COLUMN  uid uuid DEFAULT gen_random_uuid () PRIMARY key not null unique;
ALTER TABLE Operation  add COLUMN  person_id int REFERENCES person(id);
ALTER TABLE company add COLUMN owner_id int REFERENCES person(id);

update evention set balance = 0 where uid = '3332d803-728a-4afa-bf98-b769744569c7';