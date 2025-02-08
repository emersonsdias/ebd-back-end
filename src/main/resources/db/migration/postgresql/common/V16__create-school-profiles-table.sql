create table app.school_profiles (
    id bigserial not null,
    name varchar(100) not null,
    subtitle varchar(255),
    address_id uuid,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_school_profiles_addresses foreign key (address_id) references app.addresses,
    primary key (id)
)