INSERT INTO roles 
    (name, created_at, updated_at) 
VALUES 
    ('OWNER', now(), now()),
    ('ADMIN', now(), now()),
    ('USER', now(), now()),
    ('SYSTEM', now(), now());
