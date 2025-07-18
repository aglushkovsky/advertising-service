--liquibase formatted sql

--changeset aglushkovsky:insert_table_user
INSERT INTO "user" (login, password_hash, email, phone_number, role, total_rating)
VALUES ('test_user',
        '$2a$12$sGu145CigoPn4HWjLOndX.QenSaUBRZreuex2fdo3wtUriQ9dBwie', -- qwerty12345
        'example@example.com',
        '+79531234567',
        'USER',
        0.0),
       ('test_admin',
        '$2a$12$pAMuVi4R48STh59Sh6OXP.d95htFMnq6XH.f5zDyepepB/REO.MsC', -- password
        'admin_example@example.com',
        '+79202345678',
        'ADMIN',
        0.0);