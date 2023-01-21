CREATE TABLE theme
(
    id    BIGINT      NOT NULL AUTO_INCREMENT,
    name  VARCHAR(20) NOT NULL UNIQUE,
    desc  VARCHAR(255),
    price INT,
    PRIMARY KEY (id)
);
CREATE TABLE reservation
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    date     DATE,
    time     TIME,
    name     VARCHAR(20),
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theme_id) references theme (id)
);