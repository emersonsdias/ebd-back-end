create table app.classrooms (
    id bigserial primary key,
    name varchar(255) not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now()
);

create table app.students (
    id uuid not null,
    person_id uuid not null,
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_students_people foreign key (person_id) references app.people,
    constraint fk_students_classrooms foreign key (classroom_id) references app.classrooms,
    primary key (id)
);

create table app.teachers (
    id uuid not null,
    person_id uuid not null,
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_teachers_people foreign key (person_id) references app.people,
    constraint fk_teachers_classrooms foreign key (classroom_id) references app.classrooms,
    primary key (id)
);