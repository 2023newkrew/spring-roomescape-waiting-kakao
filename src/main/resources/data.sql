INSERT INTO THEME (name, desc, price) VALUES ('클래식테마', '클래식 방탈출카페', 19000);
INSERT INTO THEME (name, desc, price) VALUES ('공포테마', '공포테마 방탈출카페', 21000);

INSERT INTO SCHEDULE (theme_id, date, time) VALUES (1, '2023-01-30', '10:00');
INSERT INTO SCHEDULE (theme_id, date, time) VALUES (1, '2023-01-30', '12:00');
INSERT INTO SCHEDULE (theme_id, date, time) VALUES (1, '2023-01-30', '14:00');
INSERT INTO SCHEDULE (theme_id, date, time) VALUES (2, '2023-01-30', '10:00');
INSERT INTO SCHEDULE (theme_id, date, time) VALUES (2, '2023-01-30', '12:00');
INSERT INTO SCHEDULE (theme_id, date, time) VALUES (2, '2023-01-30', '14:00');

INSERT INTO MEMBER (username, password, name, phone, role) VALUES ('admin', 'pass', 'admin', '1111111111', 'ADMIN');