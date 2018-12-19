-- Instructor's schema

create schema cse383;
use cse383;

create table users
(
  pk int auto_increment
    primary key,
  user tinytext not null,
  password tinytext null,
  timestamp timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
  division int not null
);

create table diary
(
  pk int auto_increment
    primary key,
  userFK int null,
  itemFK int null,
  timestamp timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table diaryItems
(
  pk int auto_increment
    primary key,
  item tinytext null
);

create table tokens
(
  pk int auto_increment
    primary key,
  user tinytext not null,
  token tinytext null,
  timestamp timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);
