drop table reservation if exists;
drop table theme if exists;
drop table member if exists;

CREATE TABLE member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    is_admin boolean     not null default false,
    primary key (id)
);

create table theme
(
    id    bigint       not null auto_increment,
    name  varchar(20)  not null,
    desc  varchar(255) not null,
    price int          not null,
    primary key (id),
    unique (name)
);
create table reservation
(
    id        bigint      not null auto_increment,
    date      date        not null,
    time      time        not null,
    name      varchar(20) not null,
    theme_id  bigint      not null,
    member_id bigint      not null,
    primary key (id),
    unique (theme_id, date, time),
    foreign key (theme_id) references theme (id),
    foreign key (member_id) references member (id)
);

insert into member (id, name, phone, username, password, is_admin) values (1, '어드민', '010-1234-5678', 'admin', '1q2w3e4r!', true);
insert into member (id, name, phone, username, password, is_admin) values (2, '일반유저0', '010-0000-0000', 'user0', '1q2w3e4r!', false);
insert into member (id, name, phone, username, password, is_admin) values (3, '일반유저1', '010-0000-0001', 'user1', '1q2w3e4r!', false);

insert into theme(id, name, desc, price) values (1, '기본테마', '테마설명', 1234000 );
insert into theme(id, name, desc, price) values (2, '삭제확인테마', '테마설명', 40000 );

insert into reservation(id, date, time, name, theme_id, member_id) values (1, '1970-01-01', '12:00', '예약예약', 1, 2);
--
alter table member alter column id restart with 10;
alter table theme alter column id restart with 10;
alter table reservation alter column id restart with 10;