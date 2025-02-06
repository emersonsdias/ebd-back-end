create table app.people (
    id uuid not null,
    name varchar(255) not null,
    birthdate date,
    email varchar(255),
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);


create table app.phone_numbers (
    id uuid not null,
    area_code varchar(2) not null,
    phone_number varchar(20) not null,
    person_id uuid,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_phone_numbers_person foreign key (person_id) references app.people,
    primary key (id)
)