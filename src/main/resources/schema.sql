CREATE TABLE reservation
(
    id          bigint      not null auto_increment,
    schedule_id bigint      not null,
    member_name        varchar(40) not null,
    primary key (id)
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
    primary key (id)
);

CREATE TABLE member
(
    id       bigint      not null auto_increment,
    member_name varchar(40) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    primary key (id),
    UNIQUE (member_name)
);

CREATE TABLE member_role
(
    id bigint not null AUTO_INCREMENT,
    member_name varchar(40) not null,
    role_name varchar(40) not null,
    primary key (id)
)