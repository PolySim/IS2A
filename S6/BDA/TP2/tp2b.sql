-- Active: 1746626923856@@172.26.16.4@5432@bda_tp2b_sdesdevi

drop table if exists users;

create table users (
    id serial primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null
);

insert into users (name, email, password) values ('John Doe', 'john.doe@example.com', 'password123');
insert into users (name, email, password) values ('Jane Smith', 'jane.smith@example.com', 'password456');
insert into users (name, email, password) values ('Alice Johnson', 'alice.johnson@example.com', 'password789');

