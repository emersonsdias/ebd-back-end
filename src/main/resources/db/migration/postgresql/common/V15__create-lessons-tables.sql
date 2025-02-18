create table app.lessons (
    id bigserial not null,
    lesson_number int not null,
    lesson_date date,
    notes varchar(255),
    classroom_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_lessons_classrooms foreign key (classroom_id) references app.classrooms,
    primary key (id)
);

create table app.lesson_items (
    id bigserial not null,
    quantity int not null,
    lesson_id bigint not null,
    item_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_lesson_items_lessons foreign key (lesson_id) references app.lessons,
    constraint fk_lesson_items_items foreign key (item_id) references app.items,
    primary key (id)
);

create table app.visitors (
    id bigserial not null,
    name varchar(255) not null,
    lesson_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_visitors_lessons foreign key (lesson_id) references app.lessons,
    primary key (id)
);

create table app.offers (
    id bigserial not null,
    amount numeric(11,2) not null,
    lesson_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_offers_lessons foreign key (lesson_id) references app.lessons,
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
    constraint fk_attendances_students foreign key (student_id) references app.students,
    constraint fk_attendances_lessons foreign key (lesson_id) references app.lessons,
    primary key (id)
);

create table app.teachings (
    id bigserial not null,
    teacher_id uuid not null,
    lesson_id bigint not null,
    active boolean not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_teachings_teachers foreign key (teacher_id) references app.teachers,
    constraint fk_teachings_lessons foreign key (lesson_id) references app.lessons,
    primary key (id)
);