create table users_roles (
    role integer not null,
    user_id uuid not null
);

alter table users_roles
add constraint fk_users_roles
foreign key (user_id) references users;