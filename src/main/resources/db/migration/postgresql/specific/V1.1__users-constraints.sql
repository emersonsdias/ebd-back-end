alter table users alter column id set default gen_random_uuid();
alter table users alter column created_at set default now();
alter table users alter column updated_at set default now();
