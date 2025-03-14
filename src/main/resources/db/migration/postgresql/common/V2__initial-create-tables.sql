create table app.addresses (
    id uuid not null,
    street varchar(255) not null,
    number varchar(50),
    complement varchar(100),
    neighborhood varchar(100),
    zip_code varchar(8),
    city_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.age_ranges (
    id bigserial not null,
    name varchar(100) not null,
    min_age int,
    max_age int,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.attendances (
    id bigserial not null,
    present boolean not null,
    student_id uuid not null,
    lesson_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.classrooms (
    id bigserial primary key,
    name varchar(255) not null,
    age_range_id int,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now()
);

create table app.items (
    id bigserial not null,
    name varchar(100) not null,
    icon varchar(100) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.lessons (
    id bigserial not null,
    lesson_number int not null,
    topic varchar(255),
    lesson_date date,
    status int,
    notes varchar(255),
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.lessons_items (
    id bigserial not null,
    quantity int not null,
    lesson_id bigint not null,
    item_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.offers (
    id bigserial not null,
    amount numeric(11,2) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

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

create table app.people_types (
    type integer not null,
    person_id uuid not null
);

create table app.phone_numbers (
    id uuid not null,
    area_code varchar(2) not null,
    phone_number varchar(20) not null,
    person_id uuid,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.school_profiles (
    id bigserial not null,
    name varchar(100) not null,
    subtitle varchar(255),
    address_id uuid,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.students (
    id uuid not null,
    academic_period_start date,
    academic_period_end date,
    person_id uuid not null,
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.teachers (
    id uuid not null,
    teaching_period_start date,
    teaching_period_end date,
    person_id uuid not null,
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table app.teachings (
    id bigserial not null,
    teacher_id uuid not null,
    lesson_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

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

create table app.users_roles (
    role integer not null,
    user_id uuid not null
);

create table app.visitors (
    id bigserial not null,
    name varchar(255) not null,
    lesson_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    primary key (id)
);

create table enums.education_levels (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

create table enums.genders (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

create table enums.lesson_status (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

create table enums.marital_status (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

create table enums.people_types (
    cod int,
    description varchar(255) not null,
    translation varchar(255) not null,
    primary key (cod)
);

create table enums.users_roles (
    cod int,
    description varchar(255) not null,
    primary key (cod)
);

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
    primary key (id)
);
