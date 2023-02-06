CREATE TABLE reservation
(
    id          bigint      not null auto_increment,
    schedule_id bigint      not null,
    member_id   bigint      not null,
    status      varchar(20) not null,
    deposit     int         not null,
    created_at  datetime    not null,
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
    username varchar(20) not null,
    password varchar(20) not null,
    name     varchar(20) not null,
    phone    varchar(20) not null,
    role     varchar(20) not null,
    primary key (id)
);

CREATE TABLE reservation_waiting
(
    id          bigint not null auto_increment,
    member_id   bigint not null,
    schedule_id bigint not null,
    wait_num    int    not null,
    deposit     int    not null,
    primary key (id)
);

CREATE TABLE sales_history (
    id              bigint      not null auto_increment,
    theme_id        bigint      not null,
    reservation_id  bigint      not null,
    amount          int         not null,
    status          varchar(20) not null,
    created_at      datetime    not null,
    primary key(id)
);

CREATE TABLE reservation_sales_statistics
(
    id               bigint        not null auto_increment,
    total_sales      int           not null,
    sales_per_theme  varchar(255)  not null,
    date             datetime      not null,
    primary key(id)
);
