CREATE TABLE IF NOT EXISTS reservation
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id    bigint       not null auto_increment,
    name  varchar(20)  not null,
    desc  varchar(255) not null,
    price int          not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS schedule
(
    id       bigint not null auto_increment,
    theme_id bigint not null,
    date     date   not null,
    time     time   not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS waiting
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    primary key (id)
);

DELETE MEMBER;
DELETE RESERVATION;
DELETE THEME;
DELETE SCHEDULE;
DELETE WAITING;
