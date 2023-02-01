DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS waiting;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id),
    unique (username)
);

CREATE TABLE theme
(
    id    bigint       not null auto_increment,
    name  varchar(20)  not null,
    desc  varchar(255) not null,
    price int          not null,
    primary key (id)
);

CREATE TABLE schedule
(
    id       bigint not null auto_increment,
    theme_id bigint not null,
    date     date   not null,
    time     time   not null,
    primary key (id),
    foreign key (theme_id) references theme (id) on delete cascade,
    unique (theme_id, date, time)
);

CREATE TABLE reservation
(
    id          bigint      not null auto_increment,
    schedule_id bigint      not null,
    member_id   bigint      not null,
    status      varchar(20) not null,
    primary key (id),
    foreign key (schedule_id) references schedule (id) on delete cascade,
    foreign key (member_id) references member (id) on delete cascade,
    unique (schedule_id, member_id)
);

CREATE TABLE waiting
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    primary key (id),
    foreign key (schedule_id) references schedule (id) on delete cascade,
    foreign key (member_id) references member (id) on delete cascade,
    unique (schedule_id, member_id)
);

/* 매출은 이력 관리를 위해 레퍼런스를 제거 */
CREATE TABLE sale
(
    id              bigint      not null auto_increment,
    theme_name      varchar(20) not null,
    theme_price     int         not null,
    schedule_date   date        not null,
    schedule_time   time        not null,
    member_name     varchar(20) not null,
    member_phone    varchar(20) not null,
    primary key (id)
);