-- Test users, book data, author data, and publisher data created using LibraryDataBuilder.java, simply run it once to populate data
use dbms_library

-- Inserts sample loans for jackson@test.com account
INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-05-01', '2025-05-15', 1, 1);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-04-10', '2025-04-24', 2, 1);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-03-20', '2025-04-03', 3, 1);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-03-20', null, 4, 1);

-- Inserts sample loans for daisy@test.com account
INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-05-01', '2025-05-15', 5, 2);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-04-10', '2025-04-24', 6, 2);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-03-20', '2025-04-03', 7, 2);

INSERT INTO loan (loanDate, returnDate, bookId, userId)
VALUES ('2025-03-20', null, 8, 2);