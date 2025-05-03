package uga.edu.cs.finalProjectDBMS.Models;

import java.util.List;

public class BookDto {
    private String title;
    private int publicationYear;
    private String publisherName;
    private List<AuthorDto> authors;
    private int bookId;

    // Constructors
    public BookDto() {}

    public BookDto(String title, int bookId, int publicationYear, String publisherName, List<AuthorDto> authors) {
        this.title = title;
        this.bookId = bookId;
        this.publicationYear = publicationYear;
        this.publisherName = publisherName;
        this.authors = authors;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public List<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDto> authors) {
        this.authors = authors;
    }
}