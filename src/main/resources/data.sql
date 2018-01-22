TRUNCATE TABLE app_role;
INSERT INTO app_role (id, role_name, description) VALUES (1, 'ADMIN_USER', 'Admin User - Has permission to perform admin tasks');
INSERT INTO app_role (id, role_name, description) VALUES (2, 'STANDARD_USER', 'Standard User - Has no admin rights');

TRUNCATE TABLE app_user;
-- pasword test1234
INSERT INTO app_user (id, first_name, last_name, password, username) VALUES (1, 'Admin', 'Admin', '$2a$10$5AWyzymSnNypg9BkMOyKE.zA05GtRKHCoWimh.q2w.KAO5koBYPM6', 'admin.admin');
-- pasword test1234
INSERT INTO app_user (id, first_name, last_name, password, username) VALUES (2, 'John', 'Doe', '$2a$10$5AWyzymSnNypg9BkMOyKE.zA05GtRKHCoWimh.q2w.KAO5koBYPM6', 'john.doe');

TRUNCATE TABLE user_role;
INSERT INTO user_role(user_id, role_id) VALUES(1,1);
INSERT INTO user_role(user_id, role_id) VALUES(1,2);
INSERT INTO user_role(user_id, role_id) VALUES(2,2);
