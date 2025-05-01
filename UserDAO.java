package uga.edu.cs.finalProjectDBMS.DAO;

import uga.edu.cs.finalProjectDBMS.Models.User;

public interface UserDAO {
    boolean registerUser(User user);
    User findUserByEmail(String email);
    boolean validateUser(String email, String plainPassword);
}
