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

create table app.offers (
    id bigserial not null,
    amount numeric(11,2) not null,
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
    constraint fk_attendances_students foreign key (student_id) references app.students,
    constraint fk_attendances_lessons foreign key (lesson_id) references app.lessons,
    primary key (id)
);

create table app.attendances_items (
    id bigserial not null,
    quantity int not null,
    attendance_id bigint not null,
    item_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_attendances_items_attendances foreign key (attendance_id) references app.attendances,
    constraint fk_attendances_items_items foreign key (item_id) references app.items,
    primary key (id)
);

create table app.attendances_offers (
    id bigserial not null,
    attendance_id bigint not null,
    offer_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_attendances_offers_attendances foreign key (attendance_id) references app.attendances,
    constraint fk_attendances_offers_offers foreign key (offer_id) references app.offers,
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


create table app.visitors (
    id bigserial not null,
    name varchar(255) not null,
    lesson_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_visitors_lessons foreign key (lesson_id) references app.lessons,
    primary key (id)
);

create table app.visitors_items (
    id bigserial not null,
    quantity int not null,
    visitor_id bigint not null,
    item_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_visitors_items_visitors foreign key (visitor_id) references app.visitors,
    constraint fk_visitors_items_items foreign key (item_id) references app.items,
    primary key (id)
);

create table app.visitors_offers (
    id bigserial not null,
    visitor_id bigint not null,
    offer_id bigint not null,
    created_at timestamp(6) with time zone default now(),
    updated_at timestamp(6) with time zone default now(),
    constraint fk_visitors_offers_visitors foreign key (visitor_id) references app.visitors,
    constraint fk_visitors_offers_offers foreign key (offer_id) references app.offers,
    primary key (id)
);