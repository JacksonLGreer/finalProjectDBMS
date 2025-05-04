package uga.edu.cs.finalProjectDBMS.Models;

public class User {
    private int userId;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;

    // Constructors
    public User() {}

    // public User(String userId, String email, String firstName, String lastName) {
    //     this.email = email;
    //     //this.passwordHash = passwordHash;
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    // }
    public User(int userId, String email, String passwordHash, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(String email, String passwordHash, String firstName, String lastName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
