INSERT INTO categories (id, name) VALUES (1, 'Fantasy');
INSERT INTO categories (id, name) VALUES (2, 'Science Fiction');

INSERT INTO books (id, title, author, isbn, price, description)
VALUES (1, 'Harry Potter', 'J.K.Rowling', '123456789', 45.99, 'Magic book');
INSERT INTO books (id, title, author, isbn, price, description)
VALUES (2, 'Dune', 'Frank Herbert', '223456789', 35.99, 'Sci-fi novel');

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 2);
