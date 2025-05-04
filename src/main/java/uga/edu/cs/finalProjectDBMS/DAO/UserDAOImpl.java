package uga.edu.cs.finalProjectDBMS.DAO;

import uga.edu.cs.finalProjectDBMS.Models.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    private final Connection conn;

    public UserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean registerUser(User user) {
        String sql = "INSERT INTO user (email, password, firstName, lastName) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Email: " + user.getEmail());
            stmt.setString(1, user.getEmail());

            System.out.println("Password: "+user.getPasswordHash());
            stmt.setString(2, user.getPasswordHash());

            System.out.println("Fname: "+user.getFirstName());
            stmt.setString(3, user.getFirstName());

            System.out.println("Lname: " +user.getLastName());
            stmt.setString(4, user.getLastName());
            System.out.println("Registering: " + user.getEmail() + ", " + user.getPasswordHash());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean validateUser(String email, String plainPassword) {
        User user = findUserByEmail(email);
        return user != null ; // && PasswordUtils.checkPassword(plainPassword, user.getPasswordHash());
    }

    public boolean updateUser(User user) {
    String sql = "UPDATE user SET firstName = ?, lastName = ?, email = ? WHERE userId = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getEmail());
        stmt.setInt(4, user.getUserId());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public boolean loanBook(int bookId, int userId) {
        String sql = "INSERT INTO loan (loanDate, returnDate, bookId, userId) VALUES (CURDATE(), NULL, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error loaning book: " + e.getMessage());
            return false;
        }
    }

    public boolean isBookCurrentlyLoaned(int bookId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM loan WHERE bookId = ? AND returnDate IS NULL";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, bookId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}

    
}
