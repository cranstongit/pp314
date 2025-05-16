INSERT IGNORE INTO roles_table (role_name, authority) VALUES
    ('ROLE_USER', 'ROLE_USER'),
    ('ROLE_ADMIN', 'ROLE_ADMIN');

INSERT IGNORE INTO users (first_name, last_name, email, username, password) VALUES
    ('Иван', 'Иванов', 'ivan@example.com', 'ivan', '$2a$12$ORuFL9.4EgwDpVt.kdt8gelUk91n1R9oTWRKzQC3DIPi1zfHj0ove');

INSERT IGNORE INTO users_roles (user_id, role_id) VALUES
    ((SELECT id FROM users WHERE username = 'ivan'), (SELECT id FROM roles_table WHERE role_name = 'ROLE_ADMIN'));