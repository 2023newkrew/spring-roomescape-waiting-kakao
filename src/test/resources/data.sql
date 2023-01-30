insert into member (id, name, phone, username, password, is_admin)
values (1, '어드민', '010-1234-5678', 'admin', '1q2w3e4r!', true),
       (2, '일반유저0', '010-0000-0000', 'user0', '1q2w3e4r!', false),
       (3, '일반유저1', '010-0000-0001', 'user1', '1q2w3e4r!', false);

insert into theme(id, name, desc, price)
values (1, '기본테마', '테마설명', 1234000),
       (2, '삭제확인테마', '테마설명', 40000),
       (3, '더미테마', '테마설명', 41000);

insert into reservation(id, date, time, name, theme_id, member_id)
values (1, '1970-01-01', '12:00', '예약예약0', 1, 2);


insert into waiting(id, date, time, name, theme_id, member_id)
values (1, '1970-01-01', '12:00', '대기대기0', 3, 1),
       (2, '1970-01-01', '12:00', '대기대기1', 1, 2),
       (3, '1970-01-01', '12:00', '대기대기2', 1, 2);
--
alter table member
    alter column id restart with 10;
alter table theme
    alter column id restart with 10;
alter table reservation
    alter column id restart with 10;
alter table waiting
    alter column id restart with 10;