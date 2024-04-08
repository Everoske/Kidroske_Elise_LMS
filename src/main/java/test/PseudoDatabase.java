package test;

import domain.Book;
import domain.BookStatus;

import java.util.ArrayList;
import java.util.HashMap;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/01/2024
Name: Pseudo Database
Description:
This class only exists for testing. It is meant to
stand in for the actual SQL database
 */
public class PseudoDatabase {
    private final HashMap<Integer, Book> bookCollection;

    public PseudoDatabase() {
        bookCollection = new HashMap<>();

        // Instantiate the pseudo database with a hardcoded collection of books
        bookCollection.put(0, new Book(0, "Cinder",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(1, new Book(1, "Scarlet",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_OUT, "2024-04-24"));
        bookCollection.put(2, new Book(2, "Cress",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(3, new Book(3, "Winter",
                "Marissa Meyer", "Science Fiction", BookStatus.CHECKED_OUT, "2024-03-07"));
        bookCollection.put(4, new Book(4, "Game Programming Algorithms and Techniques",
                "Sanjay Madhav", "Non-Fiction", BookStatus.CHECKED_OUT, "2024-03-14"));
        bookCollection.put(5, new Book(5, "Game Programming Patterns",
                "Robert Nystrom", "Non-Fiction", BookStatus.CHECKED_IN));
        bookCollection.put(6, new Book(6, "Salt to the Sea",
                "Ruta Sepetys", "Historical Fiction", BookStatus.CHECKED_IN));
    }

    /*
    Name: Update Book
    Arguments: Book to update
    Returns: True or false based on success
    Description:
    Represents the UPDATE logic of a relational database
    Returns true if the update was successful
    Returns false otherwise
     */
    public boolean updateBook(Book book) {
        int primaryKey = book.getBarcode();

        // If the key does not exist, return false
        if (!bookCollection.containsKey(primaryKey)) {
            return false;
        }

        // Replace the old data with new data
        bookCollection.replace(primaryKey, book);

        return true;
    }

    /*
    Name: Delete Book
    Arguments: Book
    Returns: True or false based on success
    Description:
    Represents the DELETE logic of a relational database
    Returns true if the book was successfully deleted
    Returns false otherwise
     */
    public boolean deleteBook(Book book) {
        // Represents the primary key in the database
        int primaryKey = book.getBarcode();

        // If the database does not have the key, return false
        if (!bookCollection.containsKey(primaryKey)) {
            return false;
        }

        // Remove the key and return true
        bookCollection.remove(primaryKey);

        return true;
    }

    /*
    Name: Insert Book
    Arguments: Book
    Returns: True or false based on success
    Description:
    Represents the INSERT logic of a relational database
    Returns true if book was inserted
     */
    public boolean insertBook(Book book) {
        // Find the next primary key and assign it to the book
        int primaryKey = nextPrimaryKey();
        book.setBarcode(primaryKey);

        // Add the book to the database and return true if successful
        bookCollection.put(book.getBarcode(), book);
        return bookCollection.containsKey(book.getBarcode());
    }

    /*
    Name: Query All
    Arguments: None
    Returns: ArrayList<Book> representing all books in database
    Description:
    Represents SELECT * in a relational database
    Returns all of the Book objects stored in the HashMap
     */
    public ArrayList<Book> queryAll() {
        return new ArrayList<>(bookCollection.values());
    }

    /*
    Name: Query By Title
    Arguments: String title
    Returns: Book
    Description:
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
    Name: Query By Barcode
    Arguments: String barcode
    Returns: Book
    Description:
    Retrieves a book by its barcode using a linear search
     */
    public Book queryByBarcode(int barcode) {
        for (Book book : bookCollection.values()) {
            if (book.getBarcode() == barcode) {
                return book;
            }
        }
        return null;
    }

    /*
    Name: Next Primary Key
    Arguments: None
    Returns: int representing next primary key
    Description:
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



