-- create database if it does not exist
create database if not exists dbms_library;

-- use the database
use dbms_library

-- create the user table
create table if not exists user (
    userId int auto_increment,
    email varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (email)
);

-- create the author table
create table if not exists author (
    authorId int auto_increment,
    firstName varchar(255) not null,
    lastName varchar(255),
    primary key (authorId)
);

-- create publisher table
create table if not exists publisher (
    publisherId int auto_increment,
    name varchar(255),
    primary key (publisherId)
);

-- create the book table
create table if not exists book (
    bookId int auto_increment,
    title varchar(255),
    publicationYear int,
    publisherId int,
    primary key (bookId),
    foreign key (publisherId) references publisher(publisherId)
);

-- create loan table
create table if not exists loan (
    loanId int auto_increment,
    loanDate date,
    returnDate date,
    bookId int,
    userId int,
    primary key (loanId),
    foreign key (bookId) references book(bookId),
    foreign key (userId) references user(userId)
);

-- create bookAuthor table for many-to-many relationships
create table if not exists bookAuthor (
    bookId int,
    authorId int,
    primary key (bookId, authorId),
    foreign key (bookId) references book(bookId),
    foreign key (authorId) references author(authorId)
);

CREATE INDEX idx_bookId_loan ON loan(bookId);
CREATE INDEX idx_userId_loan ON loan(userId);