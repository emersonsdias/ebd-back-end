alter table app.attendances add constraint fk_attendances_lessons foreign key (lesson_id) references app.lessons;
alter table app.attendances add constraint fk_attendances_students foreign key (student_id) references app.students;

alter table app.classrooms add constraint fk_classrooms_age_ranges foreign key (age_range_id) references app.age_ranges;

alter table app.lessons add constraint fk_lessons_classrooms foreign key (classroom_id) references app.classrooms;
alter table app.lessons add constraint fk_enum_lesson_status foreign key (status) references enums.lesson_status;

alter table app.lessons_items add constraint fk_lessons_items_lessons foreign key (lesson_id) references app.lessons;
alter table app.lessons_items add constraint fk_lessons_items_items foreign key (item_id) references app.items;

alter table app.lessons_offers add constraint fk_lessons_offers_lessons foreign key (lesson_id) references app.lessons;
alter table app.lessons_offers add constraint fk_lessons_offers_offers foreign key (offer_id) references app.offers;

alter table app.people add constraint fk_enum_education_levels foreign key (gender) references enums.education_levels;
alter table app.people add constraint fk_enum_genders_people foreign key (gender) references enums.genders;
alter table app.people add constraint fk_enum_marital_status foreign key (marital_status) references enums.marital_status;
alter table app.people add constraint fk_people_addresses foreign key (address_id) references app.addresses;

alter table app.phone_numbers add constraint fk_phone_numbers_person foreign key (person_id) references app.people;

alter table app.students add constraint fk_students_classrooms foreign key (classroom_id) references app.classrooms;
alter table app.students add constraint fk_students_people foreign key (person_id) references app.people;

alter table app.teachers add constraint fk_teachers_classrooms foreign key (classroom_id) references app.classrooms;
alter table app.teachers add constraint fk_teachers_people foreign key (person_id) references app.people;

alter table app.teachings add constraint fk_teachings_lessons foreign key (lesson_id) references app.lessons;
alter table app.teachings add constraint fk_teachings_teachers foreign key (teacher_id) references app.teachers;

alter table app.users_roles add constraint fk_enum_users_roles foreign key (role) references enums.users_roles;
alter table app.users_roles add constraint fk_users_roles foreign key (user_id) references app.users;

alter table app.visitors add constraint fk_visitors_lessons foreign key (lesson_id) references app.lessons;

alter table location.cities add constraint fk_cities_state foreign key (state_id) references location.states;