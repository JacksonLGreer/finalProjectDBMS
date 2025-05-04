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



/**
*
*
*
*/