insert into app.users (name, email, password, active, created_at, updated_at)
values ('Admin', 'admin@admin.com', '$2a$10$7f2fJbhQD10By8uHEQiCbOHT1fNhljKbMw4sIBgdE58a3uTV8bGCy', true, current_timestamp at time zone 'UTC', current_timestamp at time zone 'UTC');

insert into app.users_roles (role, user_id) values
    (1, (select id from app.users where email like 'admin@admin.com')),
    (2, (select id from app.users where email like 'admin@admin.com'));