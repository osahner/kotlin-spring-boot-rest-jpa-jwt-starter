SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE app_role;
INSERT INTO app_role (id, role_name, description)
VALUES (1, 'ADMIN_USER', 'Admin User - Has permission to perform admin tasks');
INSERT INTO app_role (id, role_name, description)
VALUES (2, 'STANDARD_USER', 'Standard User - Has no admin rights');

TRUNCATE TABLE app_user;
-- password test1234
INSERT INTO app_user (id, first_name, last_name, password, username)
VALUES (1, 'Admin', 'Admin', '$2a$10$5AWyzymSnNypg9BkMOyKE.zA05GtRKHCoWimh.q2w.KAO5koBYPM6', 'admin.admin');
-- password test1234
INSERT INTO app_user (id, first_name, last_name, password, username)
VALUES (2, 'John', 'Doe', '$2a$10$5AWyzymSnNypg9BkMOyKE.zA05GtRKHCoWimh.q2w.KAO5koBYPM6', 'john.doe');

TRUNCATE TABLE user_role;
INSERT INTO user_role(user_id, role_id)
VALUES (1, 1);
INSERT INTO user_role(user_id, role_id)
VALUES (1, 2);
INSERT INTO user_role(user_id, role_id)
VALUES (2, 2);

TRUNCATE TABLE address;
INSERT INTO address (id, name, street, zip, city, email, tel, enabled, lastModified, options, things)
VALUES (1, 'John Doe', '4429  Jessie Street', '43215', 'Columbus', '9j92thgzfwp@fakemailgenerator.net', '520-891-3434',
        1, '2020-01-01', '{"option1":"value1"}', '["onething","anotherthing"]');

INSERT INTO address (id, name, street, zip, city, email, tel, enabled, lastModified, options, things)
VALUES (2, 'Agent Smith', '1730  Polk Street', '85705', 'Tucson', 'd3hf1lbm0z@powerencry.com', '270-466-4222', 1,
        '2020-01-01', '{"option1":"value1"}', '["onething","anotherthing"]');

SET FOREIGN_KEY_CHECKS = 1;
