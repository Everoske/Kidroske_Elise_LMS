package Domain;

import java.util.Date;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Represents an instance of a book in the library's collection.
 */
public class Book {
    private int bookId;
    private String title;
    private String barcode;
    private String author;
    private String genre;
    private BookStatus bookStatus;
    private Date dueDate;

    public Book(int bookId, String title, String barcode, String author, String genre, BookStatus bookStatus) {
        this.bookId = bookId;
        this.title = title;
        this.barcode = barcode;
        this.author = author;
        this.genre = genre;
        this.bookStatus = bookStatus;
    }

    public Book(int bookId, String title, String barcode, String author, String genre, BookStatus bookStatus, Date dueDate) {
        this(bookId, title, barcode, author, genre, bookStatus);
        this.dueDate = dueDate;
    }

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
