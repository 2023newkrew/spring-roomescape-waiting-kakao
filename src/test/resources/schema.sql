DROP TABLE IF EXISTS reservation;
CREATE TABLE reservation
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    created_datetime datetime not null default now(),
    status       varchar(20) not null default 'UN_APPROVE',
    primary key (id)
);
DROP TABLE IF EXISTS reservation_status_history;
CREATE TABLE reservation_status_history
(
    id               bigint       not null auto_increment,
    reservation_id   bigint       not null,
    before_status    varchar(20),
    after_status     varchar(20),
    changed_datetime datetime     not null default now(),
    primary key (id)
);

DROP TABLE IF EXISTS theme;
CREATE TABLE theme
(
    id    bigint       not null auto_increment,
    name  varchar(20)  not null,
    desc  varchar(255) not null,
    price int          not null,
    primary key (id)
);
DROP TABLE IF EXISTS schedule;
CREATE TABLE schedule
(
    id       bigint not null auto_increment,
    theme_id bigint not null,
    date     date   not null,
    time     time   not null,
    primary key (id)
);
DROP TABLE IF EXISTS member;
CREATE TABLE member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id)
);