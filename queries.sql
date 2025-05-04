-- This is a list of all SQL queries used in the project


-- Registration and login
/**
* registerUser - Registers a user to the platform
* Puts user's information into the database and allows for logging in
* Used on localhost:8080/register
*/
INSERT INTO user (email, password, firstName, lastName) VALUES (?, ?, ?, ?)

/**
* findUserByEmail - Used for finding a specific users information
* Selects a user based on the specific email for authentication
* Used on localhost:8080/login
*/
SELECT * FROM user WHERE email = ?

/**
* updateUser - Updates the user info in the database
* Users can edit name and email and this endpoint updates the DB
* Used on localhost:8080/profile
*/
UPDATE user SET firstName = ?, lastName = ?, email = ? WHERE userId = ?



-- Loan
/**
* loanBook - Inserts a loan into the DB
* Loans the book that is on the current info page
* Used on localhost:8080/book/info?bookId=[bookId]
*/
INSERT INTO loan (loanDate, returnDate, bookId, userId) VALUES (CURDATE(), NULL, ?, ?)

/**
* isBookCurrentlyLoaned - Checks if the input bookId is loaned
* If the book is loaned already then it should not be able to be loaned elsewhere
* Used on localhost:8080/book/info?bookId=[bookId]
*/
SELECT COUNT(*) FROM loan WHERE bookId = ? AND returnDate IS NULL

/**
* getCurrentLoans - Gets the current users active loans
* Used to create a list of all loaned books by the user
* Used on localhost:8080/loans/current
*/
SELECT loan.loanId, loan.bookId, book.title, book.publicationYear, loan.loanDate
                FROM loan
                JOIN book ON loan.bookId = book.bookId
                WHERE loan.userId = ? AND loan.returnDate IS NULL
                ORDER BY loan.loanDate DESC

/**
* getLoanHistory - Gets all loans the current user has ever had
* Used to create a list of user loan history
* Used on localhost:8080/loans/history
*/
SELECT book.bookId, book.title, book.publicationYear, loan.loanDate, loan.returnDate
                FROM loan
                JOIN book ON loan.bookId = book.bookId
                WHERE loan.userId = ?
                ORDER BY loan.loanDate DESC


-- Books

/**
* getRandomBooksWithDetails - returns 10 random books
* Used to populate dashboard with random books to try
* Used on localhost:8080/dashboard
*/
SELECT b.bookId, b.title, b.publicationYear, 
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            ORDER BY RAND()
            LIMIT 10

/**
* getBookById - returns the information on a specific book by its ID
* Used to get information about the book in the URL of the info page
* Used on http://localhost:8080/book/info?bookId=[bookId]
*/
SELECT b.bookId, b.title, b.publicationYear, 
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            WHERE b.bookId = ?


/**
* searchBooksByTitle - returns any books that have search terms in the title
* This SQL statement is dynamically made using a search result from the user
* Used on http://localhost:8080/search
*/
SELECT 
                b.bookId, b.title, b.publicationYear,
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            WHERE LOWER(b.title) LIKE ?
-- Where ? is %[searchTerm]%

/**
* searchBooksByAuthor - returns any books that have authors last name as search term
* This SQL statement is dynamically made using a search result from the user
* Used on http://localhost:8080/search
*/
SELECT 
                b.bookId, b.title, b.publicationYear,
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            WHERE LOWER(a.lastName) LIKE ?
-- Where ? is %[searchTerm]%


-- Library Data Builder queries - The queries used to insert data

-- insert into author
INSERT INTO author (firstName, lastName) VALUES (?, ?)

-- insert into publisher
INSERT INTO publisher (name) VALUES (?)

-- insert into book
INSERT INTO book (title, publicationYear, publisherId) VALUES (?, ?, ?)

-- insert into bookAuthor
INSERT INTO bookAuthor (bookId, authorId) VALUES (?, ?)