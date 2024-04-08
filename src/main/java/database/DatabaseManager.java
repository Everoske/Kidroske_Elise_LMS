package database;

import domain.Book;
import domain.BookStatus;

import java.sql.*;
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
    private final String CONNECTION_STRING = "jdbc:sqlite:library_database.db";

    public DatabaseManager() {
    }

    /*
    Name: Get All Books
    Arguments: None
    Returns: ArrayList<Book> representing all books from the library's collection
    Description:
    Retrieves all books from the internal database
     */
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();

        // Open a connection to the library database and create a statement using
        // try with resources
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement()){

            // Find all books in the library's database
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            // Create book objects for each row of the books table
            while (resultSet.next()) {
                int barcode = resultSet.getInt(BookColumns.BARCODE);
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

                if (dueDate != null) {
                    books.add(new Book(barcode, title, author, genre, bookStatus, dueDate));
                } else {
                    books.add(new Book(barcode, title, author, genre, bookStatus));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
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
        // Update the book status and due date of the Book in the database to match
        // the changes in the Book provided
        String sql = "UPDATE books SET book_status = ?, due_date = ? WHERE barcode = ?";

        // Open a connection to the library database and create a prepared statement using
        // try with resources
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement checkBook = connection.prepareStatement(sql)) {

            // Set the new book status
            checkBook.setString(1, book.getBookStatus().toString());

            // Set the due date value
            if (book.getDueDate().isEmpty()) {
                // If due date is an empty string, set the value to null in the database
                checkBook.setNull(2, Types.DATE);
            } else {
                checkBook.setString(2, book.getDueDate());
            }

            // Set the barcode to search for
            checkBook.setInt(3, book.getBarcode());

            // Run the update and return true if the update was successful
            // Update success is based on the rows affected being equal to 1
            int rowsAffected = checkBook.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        // Remove the book from the database using its barcode
        String sql = "DELETE FROM books WHERE barcode = ?";

        // Open a connection to the library database and create a prepared statement using
        // try with resources
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement deleteBook = connection.prepareStatement(sql)) {

            // Set the barcode of the book to remove
            deleteBook.setInt(1, book.getBarcode());

            // Execute the delete statement and return true if the update was successful
            // Update success is based on the rows affected being equal to 1
            int rowsAffected = deleteBook.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
        // Creates an insert statement to insert a book
        String sql = "INSERT INTO books (title, author, genre, book_status, due_date) VALUES ( ?, ?, ?, ?, ?)";

        // Open a connection to the library database and create a prepared statement using
        // try with resources
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement insertBook = connection.prepareStatement(sql)) {

            // Allows multiple statements to be grouped together as one transaction
            connection.setAutoCommit(false);

            // Loop through the provided collection of books
            // Create separate insert statements for each book provided
            // Add each statement to a group statement
            for (Book book : books) {
                insertBook.setString(1, book.getTitle());
                insertBook.setString(2, book.getAuthor());
                insertBook.setString(3, book.getGenre());
                insertBook.setString(4, book.getBookStatus().toString());
                if (book.getDueDate().isEmpty()) {
                    insertBook.setNull(5, Types.DATE);
                } else {
                    insertBook.setString(5, book.getDueDate());
                }

                // Adds the new insert statement to the group statement
                insertBook.addBatch();
            }

            // Executes the group transaction
            // Also represents an array of rows affected for each statement
            int [] batchResults = insertBook.executeBatch();

            // Commit the final transaction
            connection.commit();

            // Determine how many total rows were affected by looping through
            // the batch results
            int totalRowsAffected = 0;
            for (int rowsAffected : batchResults) {
                totalRowsAffected += rowsAffected;
            }

            // Return true if at least 1 row was changed
            if (totalRowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
    Name: Search By Title
    Arguments: String title
    Returns: Book
    Description:
    Retrieves a book from the database based on its title
     */
    public Book searchByTitle(String title) {
        Book book = null;

        // Create a query based on title
        String sql = "SELECT * FROM books WHERE title = ?";

        // Open a connection to the library database and create a prepared statement using
        // try with resources
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement selectBook = connection.prepareStatement(sql)) {

            // Insert the book's title into the Prepared Statement
            selectBook.setString(1, title);

            // Execute the query
            ResultSet resultSet = selectBook.executeQuery();

            // Construct a Book object from the row's columns
            while (resultSet.next()) {
                int barcode = resultSet.getInt(BookColumns.BARCODE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

                // Set Due Date based on whether it exists
                if (dueDate != null) {
                    book = new Book(barcode, title, author, genre, bookStatus, dueDate);
                } else {
                    book = new Book(barcode, title, author, genre, bookStatus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }

    /*
    Name: Search By Barcode
    Arguments: String barcode
    Returns: Book
    Description:
    Retrieves a book from the database based on its barcode
     */
    public Book searchByBarcode(int barcode) {
        Book book = null;

        // Create a query based on barcode
        String sql = "SELECT * FROM books WHERE barcode = ?";

        // Open a connection to the library database and create a prepared statement using
        // try with resources
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement selectBook = connection.prepareStatement(sql)) {

            // Insert the book's barcode into the Prepared Statement
            selectBook.setInt(1, barcode);

            // Execute the query
            ResultSet resultSet = selectBook.executeQuery();

            // Construct a Book object from the row's columns
            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

                // Set Due Date based on whether it exists
                if (dueDate != null) {
                    book = new Book(barcode, title, author, genre, bookStatus, dueDate);
                } else {
                    book = new Book(barcode, title, author, genre, bookStatus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }
}
