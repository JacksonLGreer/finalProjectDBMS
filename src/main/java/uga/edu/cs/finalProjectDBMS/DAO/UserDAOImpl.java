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
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
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
}
