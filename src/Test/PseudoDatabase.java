package Test;

import Domain.Book;
import Domain.BookStatus;
import java.util.*;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
This class only exists for testing. It is meant to
stand in for the actual SQL database
 */
public class PseudoDatabase {
    private final HashMap<Integer, Book> bookCollection;

    public PseudoDatabase() {
        bookCollection = new HashMap<>();

        // Instantiate the pseudo database with a hardcoded collection of books
        bookCollection.put(0, new Book(0, "Cinder", "111231333",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(1, new Book(1, "Scarlet", "1441823333",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_OUT, "2024-04-24"));
        bookCollection.put(2, new Book(2, "Cress", "55522223333",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(3, new Book(3, "Winter", "7722223663",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_OUT, "2024-03-07"));
        bookCollection.put(4, new Book(4, "Game Programming Algorithms and Techniques", "17465643423",
                "Sanjay Madhav", "Non-Fiction", BookStatus.CHECKED_OUT, "2024-03-14"));
        bookCollection.put(5, new Book(5, "Game Programming Patterns", "9999999999",
                "Robert Nystrom", "Non-Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(6, new Book(6, "Salt to the Sea", "37895345353",
                "Ruta Sepetys", "Historical Fiction", BookStatus.CHECKED_IN));
    }

    /*
    Represents the UPDATE logic of a relational database
    Returns true if the update was successful
    Returns false otherwise
     */
    public boolean updateBook(Book book) {
        int primaryKey = book.getBookId();

        // If the key does not exist, return false
        if (!bookCollection.containsKey(primaryKey)) {
            return false;
        }

        // Replace the old data with new data
        bookCollection.replace(primaryKey, book);

        return true;
    }

    /*
    Represents the DELETE logic of a relational database
    Returns true if the book was successfully deleted
    Returns false otherwise
     */
    public boolean deleteBook(Book book) {
        // Represents the primary key in the database
        int primaryKey = book.getBookId();

        // If the database does not have the key, return false
        if (!bookCollection.containsKey(primaryKey)) {
            return false;
        }

        // Remove the key and return true
        bookCollection.remove(primaryKey);

        return true;
    }

    /*
    Represents the INSERT logic of a relational database
    Returns true if book was inserted
     */
    public boolean insertBook(Book book) {
        // Find the next primary key and assign it to the book
        int primaryKey = nextPrimaryKey();
        book.setBookId(primaryKey);

        // Add the book to the database and return true if successful
        bookCollection.put(book.getBookId(), book);
        return bookCollection.containsKey(book.getBookId());
    }

    /*
    Represents SELECT * in a relational database
    Returns all of the Book objects stored in the HashMap
     */
    public ArrayList<Book> queryAll() {
        return new ArrayList<>(bookCollection.values());
    }

    /*
    Retrieves a book by its title using a linear search
     */
    public Book queryByTitle(String title) {
        for (Book book : bookCollection.values()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    /*
    Retrieves a book by its barcode using a linear search
     */
    public Book queryByBarcode(String barcode) {
        for (Book book : bookCollection.values()) {
            if (book.getBarcode().equalsIgnoreCase(barcode)) {
                return book;
            }
        }
        return null;
    }

    /*
    Represents the next primary key in the database
    Returns highest book ID + 1
     */
    private int nextPrimaryKey() {
        int nextPrimaryKey = 0;
        for (int i : bookCollection.keySet()) {
            if (i >= nextPrimaryKey) {
                nextPrimaryKey = i + 1;
            }
        }
        return nextPrimaryKey;
    }
}



