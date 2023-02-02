CREATE TABLE reservation
(
    id          bigint not null auto_increment,
    schedule_id bigint not null COMMENT 'SCHEDULE PK(FK)',
    member_id   bigint not null COMMENT 'MEMBER PK(FK)',
    primary key (id)
);

CREATE TABLE theme
(
    id    bigint       not null auto_increment,
    name  varchar(20)  not null COMMENT '테마 명',
    desc  varchar(255) not null COMMENT '테마 설명',
    price int          not null COMMENT '1인 테마 이용 가격',
    primary key (id)
);

CREATE TABLE schedule
(
    id       bigint not null auto_increment,
    theme_id bigint not null COMMENT 'THEME PK(FK)',
    date     date   not null COMMENT '스케쥴이 실행되는 날',
    time     time   not null COMMENT '스케쥴이 실행되는 시간',
    primary key (id)
);

CREATE TABLE member
(
    id       bigint      not null auto_increment,
    username varchar(20) not null COMMENT '회원의 로그인 ID',
    password varchar(20) not null COMMENT '회원의 로그인 password',
    name     varchar(20) not null COMMENT '회원의 이름',
    phone    varchar(20) not null COMMENT '회원의 Phone 번호 ex) 010-1234-5678',
    role     varchar(20) not null COMMENT '회원의 역할 ADMIN/USER',
    primary key (id)
);

CREATE TABLE reservation_waiting
(
    id          bigint  not null auto_increment,
    schedule_id bigint  not null COMMENT 'SCHEDULE PK(FK)',
    member_id   bigint  not null COMMENT 'MEMBER PK(FK)',
    wait_num    bigint  not null COMMENT '예약 대기 번호',
    wait_status tinyint not null COMMENT '예약 대기 상태'
);

