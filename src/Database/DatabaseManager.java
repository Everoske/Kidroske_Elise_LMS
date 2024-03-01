package Database;

import Domain.Book;
import Test.PseudoDatabase;

import java.util.ArrayList;
import java.util.List;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
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
    Retrieves all books from the internal database
     */
    public ArrayList<Book> getAllBooks() {
        return database.queryAll();
    }

    /*
    Updates an existing book in the database
    Returns true if the operation is successful and
    false otherwise
     */
    public boolean updateBook(Book book) {
        return database.updateBook(book);
    }

    /*
    Deletes a book from the database
    Returns true if the operation is successful and
    false otherwise
     */
    public boolean deleteBook(Book book) {
        return database.deleteBook(book);
    }

    /*
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
    Retrieves a book from the database based on its title
     */
    public Book searchByTitle(String title) {
        return database.queryByTitle(title);
    }

    /*
    Retrieves a book from the database based on its barcode
     */
    public Book searchByBarcode(String barcode) {
        return database.queryByBarcode(barcode);
    }
}
