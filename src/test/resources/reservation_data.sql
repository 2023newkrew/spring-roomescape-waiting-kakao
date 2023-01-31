insert into theme (id, name, desc, price) values (1, '테마이름', '테마설명', 1000);

insert into schedule (id, theme_id, date, time) values (1, 1, '2023-01-30', '13:00:00');
insert into schedule (id, theme_id, date, time) values (2, 1, '2023-01-30', '14:00:00');
insert into schedule (id, theme_id, date, time) values (3, 1, '2023-01-30', '15:00:00');

insert into member (id, username, password, name, phone, role) values (1, 'username1', 'password', '이름', '010-1234-5678', 'member');
insert into member (id, username, password, name, phone, role) values (2, 'username2', 'password', '이름', '010-1234-5678', 'member');
insert into member (id, username, password, name, phone, role) values (3, 'username3', 'password', '이름', '010-1234-5678', 'member');

insert into reservation (id, schedule_id, member_id, created_datetime) values (1, 1, 1, '2023-01-01 13:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (2, 2, 1, '2023-01-01 13:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (3, 3, 1, '2023-01-01 14:00:00');

insert into reservation (id, schedule_id, member_id, created_datetime) values (4, 1, 2, '2023-01-01 14:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (5, 2, 2, '2023-01-01 14:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (6, 3, 2, '2023-01-01 13:00:00');

insert into reservation (id, schedule_id, member_id, created_datetime) values (7, 1, 3, '2023-01-01 15:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (8, 2, 3, '2023-01-01 15:00:00');
insert into reservation (id, schedule_id, member_id, created_datetime) values (9, 3, 3, '2023-01-01 15:00:00');
