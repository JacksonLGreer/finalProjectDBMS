package uga.edu.cs.finalProjectDBMS.services;

import org.springframework.stereotype.Service;
import uga.edu.cs.finalProjectDBMS.Models.BookDto;
import uga.edu.cs.finalProjectDBMS.Models.AuthorDto;

import java.sql.*;
import java.util.*;

@Service
public class BookService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:33306/dbms_library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "mysqlpass";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public List<BookDto> getRandomBooksWithDetails() {
        List<BookDto> books = new ArrayList<>();

        final String sql = """
            SELECT 
                b.bookId, b.title, b.publicationYear, 
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            ORDER BY RAND()
            LIMIT 10
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, BookDto> bookMap = new LinkedHashMap<>();

            while (rs.next()) {
                int bookId = rs.getInt("bookId");

                BookDto book = bookMap.get(bookId);
                if (book == null) {
                    book = new BookDto();
                    book.setBookId(bookId);
                    book.setTitle(rs.getString("title"));
                    book.setPublicationYear(rs.getInt("publicationYear"));
                    book.setPublisherName(rs.getString("publisherName"));
                    book.setAuthors(new ArrayList<>());
                    bookMap.put(bookId, book);
                }

                int authorId = rs.getInt("authorId");
                if (authorId > 0) {
                    AuthorDto author = new AuthorDto();
                    author.setFirstName(rs.getString("authorFirstName"));
                    author.setLastName(rs.getString("authorLastName"));
                    book.getAuthors().add(author);
                }
            }

            books.addAll(bookMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
            // optionally log or throw custom exception
        }

        return books;
    }

    /**
     * This returns a specific books info based on its ID
     * @param bookId
     * @return Specific Book DTO
     */
    public BookDto getBookById(int bookId) {
        final String sql = """
            SELECT 
                b.bookId, b.title, b.publicationYear, 
                p.name AS publisherName,
                a.authorId, a.firstName AS authorFirstName, a.lastName AS authorLastName
            FROM book b
            LEFT JOIN publisher p ON b.publisherId = p.publisherId
            LEFT JOIN bookAuthor ba ON b.bookId = ba.bookId
            LEFT JOIN author a ON ba.authorId = a.authorId
            WHERE b.bookId = ?
        """;
    
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
    
            BookDto book = null;
            List<AuthorDto> authors = new ArrayList<>();
    
            while (rs.next()) {
                if (book == null) {
                    book = new BookDto();
                    book.setTitle(rs.getString("title"));
                    book.setBookId(rs.getInt("bookId"));
                    book.setPublicationYear(rs.getInt("publicationYear"));
                    book.setPublisherName(rs.getString("publisherName"));
                    book.setAuthors(authors);
                }
    
                int authorId = rs.getInt("authorId");
                if (authorId > 0) {
                    AuthorDto author = new AuthorDto();
                    author.setFirstName(rs.getString("authorFirstName"));
                    author.setLastName(rs.getString("authorLastName"));
                    authors.add(author);
                }
            }
    
            return book;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}