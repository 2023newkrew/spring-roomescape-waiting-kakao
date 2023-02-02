CREATE TABLE member
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    name     VARCHAR(20) NOT NULL,
    phone    VARCHAR(20) NOT NULL,
    role     VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id    BIGINT      NOT NULL AUTO_INCREMENT,
    name  VARCHAR(20) NOT NULL UNIQUE,
    desc  VARCHAR(255),
    price INT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE schedule
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    date     DATE   NOT NULL,
    time     TIME   NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    UNIQUE (date, time, theme_id)
);

CREATE TABLE reservation
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    member_id   BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    status      varchar(20) not null default 'UNAPPROVED',
    deleted     bit    not null default false,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);


CREATE TABLE waiting
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    member_id   BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id),
    UNIQUE (member_id, schedule_id)
);
