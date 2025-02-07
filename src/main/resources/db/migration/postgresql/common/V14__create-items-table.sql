create table app.items (
    id bigserial not null,
    name varchar(100) not null,
    icon varchar(100) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
)
