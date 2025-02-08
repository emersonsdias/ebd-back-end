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

--alter table app.classrooms add column age_range_id int;

alter table app.classrooms add constraint fk_classrooms_age_ranges foreign key (age_range_id) references app.age_ranges;