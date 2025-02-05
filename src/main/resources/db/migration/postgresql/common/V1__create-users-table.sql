create schema app;

create table app.users (
    id uuid not null,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);