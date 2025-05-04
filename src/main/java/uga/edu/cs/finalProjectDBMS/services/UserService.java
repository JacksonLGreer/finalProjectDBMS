package uga.edu.cs.finalProjectDBMS.services;

import org.springframework.stereotype.Service;
import uga.edu.cs.finalProjectDBMS.DAO.UserDAOImpl;
import uga.edu.cs.finalProjectDBMS.Models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

@Service
public class UserService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:33306/dbms_library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "mysqlpass";

    private User loggedInUser = null;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    /** 
    public boolean authenticate(String email, String password) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            return dao.validateUser(email, password);
        }
    }
        */

    public boolean registerUser(String email, String password, String firstName, String lastName) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            User user = new User(email, passwordEncoder.encode(password), firstName, lastName);
            return dao.registerUser(user);
        }
    }

    public void unAuthenticate() {
        // You can invalidate a session or clear auth here if needed
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }


    public boolean loanBookToUser(int bookId, int userId) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            return dao.loanBook(bookId, userId);
        }
    }
    
    /**
     * Authenticate user given the username and the password and
     * stores user object for the logged in user in session scope.
     * Returns true if authentication is succesful. False otherwise.
     */
    public boolean authenticate(String email, String password) throws SQLException {
        // Note the ? mark in the query. It is a place holder that we will later replace.
        final String sql = "select * from user where email = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with username.
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Traverse the result rows one at a time.
                // Note: This specific while loop will only run at most once 
                // since username is unique.
                while (rs.next()) {
                    // Note: rs.get.. functions access attributes of the current row.
                    String storedPasswordHash = rs.getString("password");
                    boolean isPassMatch = passwordEncoder.matches(password, storedPasswordHash);
                    // Note: 
                    if (isPassMatch) {
                        int userId = rs.getInt("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");

                        // Initialize and retain the logged in user.
                        loggedInUser = new User(userId, email, password, firstName, lastName);
                    }
                    return isPassMatch;
                }
            }
        }
        return false;
    }
}
