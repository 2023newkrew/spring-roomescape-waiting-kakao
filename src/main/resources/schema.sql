drop table sales if exists;
drop table waiting if exists;
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
    status    int         not null,
    primary key (id),
    unique (theme_id, date, time),
    foreign key (theme_id) references theme (id),
    foreign key (member_id) references member (id)
);

create table waiting
(
    id        bigint      not null auto_increment,
    date      date        not null,
    time      time        not null,
    name      varchar(20) not null,
    theme_id  bigint      not null,
    member_id bigint      not null,
    primary key (id),
    foreign key (theme_id) references theme (id),
    foreign key (member_id) references member (id)
);


create table sales
(
    created_at      datetime          not null default now(),
    change          numeric(19, 4)    not null,
    reason          varchar(128)      ,
    reservation_id  varchar(128)      ,
    foreign key (reservation_id) references reservation (id)
);