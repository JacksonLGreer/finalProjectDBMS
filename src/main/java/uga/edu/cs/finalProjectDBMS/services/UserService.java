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

    public boolean registerUser(String email, String password, String firstName, String lastName) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            User user = new User(email, passwordEncoder.encode(password), firstName, lastName);
            return dao.registerUser(user);
        }
    }

    public boolean authenticate(String email, String password) throws SQLException {
        final String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String storedPasswordHash = rs.getString("password");
                    boolean isPassMatch = passwordEncoder.matches(password, storedPasswordHash);
                    if (isPassMatch) {
                        int userId = rs.getInt("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");
                        loggedInUser = new User(userId, email, password, firstName, lastName);
                    }
                    return isPassMatch;
                }
            }
        }
        return false;
    }

    public void updateUser(String firstName, String lastName, String email) throws SQLException {
    if (loggedInUser == null) return;

    try (Connection conn = getConnection()) {
        UserDAOImpl dao = new UserDAOImpl(conn);
        loggedInUser.setFirstName(firstName);
        loggedInUser.setLastName(lastName);
        loggedInUser.setEmail(email);
        dao.updateUser(loggedInUser);
    }
}

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void unAuthenticate() {
        loggedInUser = null;
    }

    public boolean loanBookToUser(int bookId, int userId) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            if (dao.isBookCurrentlyLoaned(bookId)) {
                return false; // don't allow double-loaning
        }
        return dao.loanBook(bookId, userId);
    }
}

    public void returnLoan(int loanId) throws SQLException {
        String sql = "UPDATE loan SET returnDate = CURDATE() WHERE loanId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            stmt.executeUpdate();
        }
    }
}
