create table app.addresses (
    id uuid not null,
    street varchar(255) not null,
    number varchar(50),
    complement varchar(100),
    neighborhood varchar(100),
    zip_code varchar(8),
    city_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);