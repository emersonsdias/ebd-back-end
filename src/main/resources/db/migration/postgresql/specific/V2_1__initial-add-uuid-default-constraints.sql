alter table app.addresses alter column id set default gen_random_uuid();

alter table app.people alter column id set default gen_random_uuid();

alter table app.phone_numbers alter column id set default gen_random_uuid();

alter table app.students alter column id set default gen_random_uuid();

alter table app.teachers alter column id set default gen_random_uuid();

alter table app.users alter column id set default gen_random_uuid();
