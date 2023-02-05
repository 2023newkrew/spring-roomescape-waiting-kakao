CREATE TABLE IF NOT EXISTS reservation
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    status      varchar(20) not null,
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
    primary key (id),
    unique(username)
);

CREATE TABLE IF NOT EXISTS reservation_waiting
(
    id          bigint not null auto_increment,
    schedule_id bigint not null,
    member_id   bigint not null,
    reg_time    datetime not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS sales
(
    id          bigint not null auto_increment,
    reservation_id bigint not null,
    amount      int not null,
    primary key (id)
);


