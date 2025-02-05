create schema enums;

create table enums.enum_users_roles (
    cod int,
    description varchar(255) not null,
    primary key (cod)
);

insert into enums.enum_users_roles (cod, description) values
    (1, 'ADMIN'),
    (2, 'TEACHER');

create table app.users_roles (
    role integer not null,
    user_id uuid not null
);

alter table app.users_roles add constraint fk_enum_users_roles foreign key (role) references enums.enum_users_roles;
alter table app.users_roles add constraint fk_users_roles foreign key (user_id) references app.users;

