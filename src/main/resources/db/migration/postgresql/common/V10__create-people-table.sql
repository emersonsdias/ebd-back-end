create table app.people (
    id uuid not null,
    name varchar(255) not null,
    birthdate date,
    email varchar(255),
    gender int,
    education_level int,
    marital_status int,
    address_id uuid,
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
    primary key (id)
);

create table enums.genders (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

insert into enums.genders (cod, description, translation) values
    (1, 'MALE', 'Masculino'),
    (2, 'FEMALE', 'Feminino');

create table enums.education_levels (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

insert into enums.education_levels (cod, description, translation) values
    (1, 'ELEMENTARY', 'Ensino fundamento incompleto'),
    (2, 'MIDDLE_SCHOOL', 'Ensino fundamental'),
    (3, 'HIGH_SCHOOL', 'Ensino médio'),
    (4, 'TECHNICAL', 'Ensino técnico'),
    (5, 'INCOMPLETE_HIGHER_EDUCATION', 'Superior incompleto'),
    (6, 'HIGHER_EDUCATION', 'Superior completo'),
    (7, 'POSTGRADUATE', 'Pós graduação');

create table enums.marital_status (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

insert into enums.marital_status (cod, description, translation) values
    (1, 'SINGLE', 'Solteiro(a)'),
    (2, 'MARRIED', 'Casado(a)'),
    (3, 'DIVORCED', 'Divorciado(a)'),
    (4, 'WIDOWED', 'Viúvo(a)');

alter table app.phone_numbers add constraint fk_phone_numbers_person foreign key (person_id) references app.people;
alter table app.people add constraint fk_people_addresses foreign key (address_id) references app.addresses;
alter table app.people add constraint fk_enum_genders_people foreign key (gender) references enums.genders;
alter table app.people add constraint fk_enum_education_levels foreign key (gender) references enums.education_levels;
alter table app.people add constraint fk_enum_marital_status foreign key (marital_status) references enums.marital_status;