insert into theme (name, desc, price) values ('테마이름', '테마설명', 1000);

insert into schedule (theme_id, date, time) values (1, '2023-01-30', '13:00:00');
insert into schedule (theme_id, date, time) values (1, '2023-01-30', '14:00:00');
insert into schedule (theme_id, date, time) values (1, '2023-01-30', '15:00:00');
insert into schedule (theme_id, date, time) values (1, '2023-01-30', '16:00:00');

insert into member (username, password, name, phone, role) values ('username1', 'password', '이름', '010-1234-5678', 'member');
insert into member (username, password, name, phone, role) values ('username2', 'password', '이름', '010-1234-5678', 'member');
insert into member (username, password, name, phone, role) values ('username3', 'password', '이름', '010-1234-5678', 'member');

insert into reservation (schedule_id, member_id, created_datetime) values (1, 1, '2023-01-01 13:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (2, 1, '2023-01-01 13:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (3, 1, '2023-01-01 14:00:00');

insert into reservation (schedule_id, member_id, created_datetime) values (1, 2, '2023-01-01 14:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (2, 2, '2023-01-01 14:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (3, 2, '2023-01-01 13:00:00');

insert into reservation (schedule_id, member_id, created_datetime) values (1, 3, '2023-01-01 15:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (2, 3, '2023-01-01 15:00:00');
insert into reservation (schedule_id, member_id, created_datetime) values (3, 3, '2023-01-01 15:00:00');
