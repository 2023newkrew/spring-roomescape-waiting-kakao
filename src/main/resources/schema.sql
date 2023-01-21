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
    theme_id BIGINT NOT NULL,
    date     DATE   NOT NULL,
    time     TIME   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE TABLE reservation
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
--     member_id   BIGINT NOT NULL,
    theme_id BIGINT      NOT NULL,
    date     DATE        NOT NULL,
    time     TIME        NOT NULL,
--     schedule_id BIGINT NOT NULL,
    name     VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
--     FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
--     FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);
