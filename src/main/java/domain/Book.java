package domain;

/**
 * This class represents an instance of a book in the library database.
 * @author Elise Kidroske
 */
public class Book {
    /** Unique barcode used for storage in the library database.*/
    private int barcode;
    /** The title of the book.*/
    private String title;
    /** The author of the book.*/
    private String author;
    /** The genre of the book.*/
    private String genre;
    /** Determines whether the book is checked in or checked out.*/
    private BookStatus bookStatus;
    /** The due date of the book.*/
    private String dueDate;

    public Book(int barcode, String title, String author, String genre, BookStatus bookStatus) {
        this(barcode, title, author, genre, bookStatus, "");
    }

    public Book(int barcode, String title, String author, String genre, BookStatus bookStatus, String dueDate) {
        this.barcode = barcode;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.bookStatus = bookStatus;
        this.dueDate = dueDate;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
