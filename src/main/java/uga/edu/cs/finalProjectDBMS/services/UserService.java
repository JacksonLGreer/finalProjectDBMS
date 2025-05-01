package uga.edu.cs.finalProjectDBMS.services;

import org.springframework.stereotype.Service;
import uga.edu.cs.finalProjectDBMS.DAO.UserDAOImpl;
import uga.edu.cs.finalProjectDBMS.Models.User;
import uga.edu.cs.finalProjectDBMS.Utils.PasswordUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class UserService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/dbms_library";
    private static final String DB_USER = "yourUsername";
    private static final String DB_PASS = "yourPassword";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public boolean authenticate(String email, String password) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            return dao.validateUser(email, password);
        }
    }

    public boolean registerUser(String email, String password, String firstName, String lastName) throws SQLException {
        try (Connection conn = getConnection()) {
            UserDAOImpl dao = new UserDAOImpl(conn);
            User user = new User(email, PasswordUtils.hashPassword(password), firstName, lastName);
            return dao.registerUser(user);
        }
    }

    public void unAuthenticate() {
        // You can invalidate a session or clear auth here if needed
    }
}
