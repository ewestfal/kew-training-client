create user 'trnapp'@'localhost' identified by 'trnapp';
create database trnapp;
grant all on trnapp.* to 'trnapp'@'localhost';