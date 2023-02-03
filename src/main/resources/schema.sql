CREATE TABLE member
(
    id       bigint      NOT NULL auto_increment,
    username varchar(20) NOT NULL,
    password varchar(20) NOT NULL,
    name     varchar(20) NOT NULL,
    phone    varchar(20) NOT NULL,
    role     varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id    bigint       NOT NULL auto_increment,
    name  varchar(20)  NOT NULL,
    desc  varchar(255) NOT NULL,
    price int          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE schedule
(
    id       bigint NOT NULL auto_increment,
    theme_id bigint NOT NULL,
    date     date   NOT NULL,
    time     time   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id          bigint NOT NULL auto_increment,
    schedule_id bigint NOT NULL,
    member_id   bigint NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE reservation_waiting
(
    id          bigint NOT NULL auto_increment,
    schedule_id bigint NOT NULL,
    member_id   bigint NOT NULL,
    wait_num    bigint NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);

