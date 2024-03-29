--GENRES
INSERT INTO GENRE (NAME)
VALUES ('Children''s books');
INSERT INTO GENRE (NAME)
VALUES ('Education');
INSERT INTO GENRE (NAME)
VALUES ('Action');
INSERT INTO GENRE (NAME)
VALUES ('Non-Fiction');
INSERT INTO GENRE (NAME)
VALUES ('Fiction');

--AUTHORS
INSERT INTO AUTHOR (FIRST_NAME, LAST_NAME)
VALUES ('Gayle', 'McDowell');
INSERT INTO AUTHOR (FIRST_NAME, LAST_NAME)
VALUES ('Loreen', 'Leedy');
INSERT INTO AUTHOR (FIRST_NAME, LAST_NAME)
VALUES ('Candace', 'Fleming');

--BOOKS
INSERT INTO BOOK (NAME, PRICE, PUBLISHER)
VALUES ('Cracking the Coding Interview', 39.99, 'Career Cup, LLC');
INSERT INTO BOOK (NAME, PRICE, PUBLISHER)
VALUES ('Seeing Symmetry', 7.99, 'Holiday House Publishing, Inc.');
INSERT INTO BOOK (NAME, PRICE, PUBLISHER)
VALUES ('Papa''s Mechanical Fish', 8.99, 'Farrar Straus Giroux Books');

--BOOK_AUTHOR
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID)
VALUES (1, 1);
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID)
VALUES (2, 2);
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID)
VALUES (3, 3);

--BOOL_GENRE
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID)
VALUES (1, 2);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID)
VALUES (2, 1);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID)
VALUES (3, 1);



