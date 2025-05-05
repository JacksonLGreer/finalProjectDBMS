Library Management System - Final Project
-----------------------------------------

Team Members:
-------------
1. Jackson Greer
   - Setup Home page and spring and maven
   - Created SQL Tables
   - Created register and login pages
   - Created Library Data Builder
   - Book linkage on homepage and info page 

2. Daisy Gryborski
   - Developed all of the loan integration
   - Worked on ER modeling and schema conversion

3. Miroslav Ostrovski
   - Created dashboard, login, registration, and profile controllers
   - Created a User service and model along with UserDAO and UserDAOImpl

Technologies Used:
------------------
- Java 17
- Spring Boot
- Spring MVC
- Maven
- MySQL 8 (Docker-based)
- Mustache templates
- HTML/CSS 
- Standard Java libraries

Note: No external libraries

Database Details:
-----------------
- Database name: dbms_library
- JDBC username: root
- JDBC password: mysqlpass

Test Users:
-----------
These are created in the LibraryDataBuilder because the passwords need to be hashed before inserting
1) email: jackson@test.com password: password
2) email: daisy@test.com password: password
3) email: jackson@test.com password: password
