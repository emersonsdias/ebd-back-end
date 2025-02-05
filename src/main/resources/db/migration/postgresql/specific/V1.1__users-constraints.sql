alter table app.users alter column id set default gen_random_uuid();
alter table app.users alter column created_at set default now();
alter table app.users alter column updated_at set default now();
