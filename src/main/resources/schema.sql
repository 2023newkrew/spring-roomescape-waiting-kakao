drop table reservation if exists;
drop table theme if exists;

create table theme
(
    id          bigint          not null    auto_increment      ,
    name        varchar(20)     not null                        ,
    desc        varchar(255)    not null                        ,
    price       int             not null                        ,
    primary key (id),
    unique (name)
);
create table reservation
(
    id          bigint          not null    auto_increment      ,
    date        date            not null                        ,
    time        time            not null                        ,
    name        varchar(20)     not null                        ,
    theme_id    bigint          not null                        ,
    primary key (id),
    foreign key (theme_id) references theme(id)
);
