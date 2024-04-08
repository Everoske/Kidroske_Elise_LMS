package database;

import domain.Book;
import test.PseudoDatabase;

import java.util.ArrayList;
import java.util.List;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/01/2024
Name: Database Manager
Description:
Represents the database layer of the application. This class
directly interacts with the internal database. It also exchanges
information to and from the Application layer and updates
the database accordingly.
 */
public class DatabaseManager {
    private final PseudoDatabase database;

    public DatabaseManager() {
        database = new PseudoDatabase();
    }

    /*
    Name: Get All Books
    Arguments: None
    Returns: ArrayList<Book> representing all books from the library's collection
    Description:
    Retrieves all books from the internal database
     */
    public ArrayList<Book> getAllBooks() {
        return database.queryAll();
    }

    /*
    Name: Update Book
    Arguments: Book to update
    Returns: True or false based on success
    Description:
    Updates an existing book in the database
    Returns true if the operation is successful and
    false otherwise
     */
    public boolean updateBook(Book book) {
        return database.updateBook(book);
    }

    /*
    Name: Delete Book
    Arguments: Book to delete
    Returns: True or false based on success
    Description
    Deletes a book from the database
    Returns true if the operation is successful and
    false otherwise
     */
    public boolean deleteBook(Book book) {
        return database.deleteBook(book);
    }

    /*
    Name: Insert Books
    Arguments: List<Book> representing books to add
    Returns: True if more 0 books added, false otherwise
    Description:
    Inserts a list of books into the database
    Returns true if at least one book was added to
    the database
    */
    public boolean insertBooks(List<Book> books) {
        // Keeps track of successful insertions
        int successfulInsertions = 0;
        for (Book book : books) {
            if (database.insertBook(book)) {
                successfulInsertions++;
            }
        }

        // Returns true if there is at least one successful insertion
        return successfulInsertions > 0;
    }

    /*
    Name: Search By Title
    Arguments: String title
    Returns: Book
    Description:
    Retrieves a book from the database based on its title
     */
    public Book searchByTitle(String title) {
        return database.queryByTitle(title);
    }

    /*
    Name: Search By Barcode
    Arguments: String barcode
    Returns: Book
    Description:
    Retrieves a book from the database based on its barcode
     */
    public Book searchByBarcode(int barcode) {
        return database.queryByBarcode(barcode);
    }
}
