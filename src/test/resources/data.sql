INSERT INTO member (member_name, password, name, phone) VALUES ('reservation_exist_member1', 'password', 'name', '010-1234-5678');
INSERT INTO member (member_name, password, name, phone) VALUES ('reservation_exist_member2', 'password', 'name', '010-1234-5678');
INSERT INTO member (member_name, password, name, phone) VALUES ('no_reservation_exist_member', 'password', 'name', '010-1234-5678');
INSERT INTO member (member_name, password, name, phone) VALUES ('role_admin', 'password', 'name', '010-1234-5678');
INSERT INTO member (member_name, password, name, phone) VALUES ('role_member', 'password', 'name', '010-1234-5678');

INSERT INTO member_role (member_name, role_name) VALUES ('reservation_exist_member1', 'ROLE_MEMBER');
INSERT INTO member_role (member_name, role_name) VALUES ('reservation_exist_member2', 'ROLE_MEMBER');
INSERT INTO member_role (member_name, role_name) VALUES ('no_reservation_exist_member', 'ROLE_MEMBER');
INSERT INTO member_role (member_name, role_name) VALUES ('role_admin', 'ROLE_ADMIN');
INSERT INTO member_role (member_name, role_name) VALUES ('role_member', 'ROLE_MEMBER');

INSERT INTO theme (name, desc, price) VALUES ('theme1', 'theme1', 1000);
INSERT INTO theme (name, desc, price) VALUES ('theme2', 'theme2', 2000);

INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '12:00');
INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '12:30');
INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '13:00');

INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '12:00');
INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '12:30');
INSERT INTO schedule (theme_id, date, time) VALUES (1, '2022-11-11', '13:00');

INSERT INTO reservation (schedule_id, member_name) VALUES (1, 'reservation_exist_member1');
INSERT INTO reservation (schedule_id, member_name) VALUES (2, 'reservation_exist_member1');
INSERT INTO reservation (schedule_id, member_name) VALUES (3, 'reservation_exist_member1');

INSERT INTO reservation (schedule_id, member_name) VALUES (4, 'reservation_exist_member2');
INSERT INTO reservation (schedule_id, member_name) VALUES (5, 'reservation_exist_member2');