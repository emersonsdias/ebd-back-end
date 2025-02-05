create schema location;

create table location.states (
    id bigserial not null,
    name varchar(255) not null,
    abbreviation varchar(2) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table location.cities (
    id bigserial not null,
    name varchar(255) not null,
    state_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_cities_state foreign key (state_id) references location.states,
    primary key (id)
);
