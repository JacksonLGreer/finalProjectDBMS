package uga.edu.cs.finalProjectDBMS.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LibraryDataBuilder {
    static final String JDBC_URL = "jdbc:mysql://localhost:33306/dbms_library";
    static final String JDBC_USER = "root";
    static final String JDBC_PASSWORD = "mysqlpass";

    static final String[] FIRST_NAMES = {"John", "Emily", "David", "Sarah", "Michael", "Emma", "James", "Olivia", "William", "Ava"};
    static final String[] LAST_NAMES = {"Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Taylor", "Moore", "Anderson", "Thomas"};
    static final String[] PUBLISHERS = {"Penguin", "HarperCollins", "Simon & Schuster", "Macmillan", "Hachette", "Random House", "Oxford Press", "Cambridge Press", "Bloomsbury", "Tor"};
    static final String[] TITLE_WORDS = {"Shadow", "Fire", "Secret", "Dream", "Night", "Light", "Wind", "Stone", "Legacy", "Chronicle", "War", "Hope", "Truth", "Journey"};

    static final Random rand = new Random();

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(false); // batch insert optimization

            insertAuthors(conn, 20);
            insertPublishers(conn);
            insertBooksAndAuthors(conn, 1000);
            insertTestUsers(conn);

            conn.commit();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Data already populated");
        }
    }

    // Endpoint to create random authors
    static void insertAuthors(Connection conn, int count) throws SQLException {
        String sql = "INSERT INTO author (firstName, lastName) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < count; i++) {
                ps.setString(1, getRandom(FIRST_NAMES));
                ps.setString(2, getRandom(LAST_NAMES));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // Endpoint to insert the publishers
    static void insertPublishers(Connection conn) throws SQLException {
        String sql = "INSERT INTO publisher (name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String name : PUBLISHERS) {
                ps.setString(1, name);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // Endpoint to create random books and assign them authors and publishers
    static void insertBooksAndAuthors(Connection conn, int bookCount) throws SQLException {
        String bookSql = "INSERT INTO book (title, publicationYear, publisherId) VALUES (?, ?, ?)";
        String bookAuthorSql = "INSERT INTO bookAuthor (bookId, authorId) VALUES (?, ?)";

        try (
            PreparedStatement bookPs = conn.prepareStatement(bookSql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement bookAuthorPs = conn.prepareStatement(bookAuthorSql)
        ) {
            for (int i = 0; i < bookCount; i++) {
                String title = getRandomTitle();
                int year = getRandomInt(1950, 2023);
                int publisherId = getRandomInt(1, PUBLISHERS.length);

                bookPs.setString(1, title);
                bookPs.setInt(2, year);
                bookPs.setInt(3, publisherId);
                bookPs.executeUpdate();

                try (ResultSet rs = bookPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        int bookId = rs.getInt(1);
                        int authorId = getRandomInt(1, 20);
                        bookAuthorPs.setInt(1, bookId);
                        bookAuthorPs.setInt(2, authorId);
                        bookAuthorPs.executeUpdate();
                    }
                }
            }
        }
    }

    // Inserts the test users with hashed passwords
    static void insertTestUsers(Connection conn) throws SQLException {
        String sql = "INSERT INTO user (email, password, firstName, lastName) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Object[][] users = {
                {"jackson@test.com", "password", "Jackson", "Greer"},
                {"daisy@test.com", "password", "Daisy", "Gryboski"},
                {"miro@example.com", "password", "Miroslav", "Ostrovski"}
            };

            for (Object[] user : users) {
                ps.setString(1, (String) user[0]);
                ps.setString(2, encoder.encode((String) user[1]));
                ps.setString(3, (String) user[2]);
                ps.setString(4, (String) user[3]);
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Test users inserted successfully.");
        }
    }


    static String getRandom(String[] array) {
        return array[rand.nextInt(array.length)];
    }

    static int getRandomInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    static String getRandomTitle() {
        int words = rand.nextInt(2) + 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words; i++) {
            if (i > 0) sb.append(" ");
            sb.append(getRandom(TITLE_WORDS));
        }
        return sb.toString().replace("'", "''");
    }






}
