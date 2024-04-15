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

/**
 * This class represents the database layer of the library management system.
 * This class directly interacts with the internal library database. It also exchanges
 * information to and from the application layer and the database.
 * @author Elise Kidroske
 */
public class DatabaseManager {
    private final String CONNECTION_STRING = "jdbc:sqlite:data/library_database.db";

    public DatabaseManager() {
    }

    /**
     * Retrieves all books from the library database.
     * @return Book ArrayList representing all books from the library database.
     */
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

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

    /**
     * Updates an existing book in the database.
     * @param book Book object to be updated.
     * @return True if successful, false otherwise.
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET book_status = ?, due_date = ? WHERE barcode = ?";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement checkBook = connection.prepareStatement(sql)) {

            checkBook.setString(1, book.getBookStatus().toString());

            if (book.getDueDate().isEmpty()) {
                checkBook.setNull(2, Types.DATE);
            } else {
                checkBook.setString(2, book.getDueDate());
            }

            checkBook.setInt(3, book.getBarcode());

            int rowsAffected = checkBook.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an existing book from the database.
     * @param book Book object to be deleted.
     * @return True if successful, false otherwise.
     */
    public boolean deleteBook(Book book) {
        String sql = "DELETE FROM books WHERE barcode = ?";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement deleteBook = connection.prepareStatement(sql)) {

            deleteBook.setInt(1, book.getBarcode());

            int rowsAffected = deleteBook.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Inserts a list of books into the database.
     * @param books List of Book objects to be added to the library database.
     * @return True if successful, false otherwise.
     */
    public boolean insertBooks(List<Book> books) {
        String sql = "INSERT INTO books (title, author, genre, book_status, due_date) VALUES ( ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement insertBook = connection.prepareStatement(sql)) {

            // Allows multiple statements to be grouped together as one transaction
            connection.setAutoCommit(false);

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
            int [] batchResults = insertBook.executeBatch();

            // Commit the final transaction
            connection.commit();

            int totalRowsAffected = 0;
            for (int rowsAffected : batchResults) {
                totalRowsAffected += rowsAffected;
            }

            if (totalRowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves a book from the database based on its title.
     * @param title String title to query.
     * @return Book object that matches the provided title.
     */
    public Book searchByTitle(String title) {
        Book book = null;

        String sql = "SELECT * FROM books WHERE title = ?";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement selectBook = connection.prepareStatement(sql)) {

            selectBook.setString(1, title);

            ResultSet resultSet = selectBook.executeQuery();

            while (resultSet.next()) {
                int barcode = resultSet.getInt(BookColumns.BARCODE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

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

    /**
     * Retrieves a book from the database based on its barcode.
     * @param barcode Int barcode to query.
     * @return Book object that matches the provided barcode.
     */
    public Book searchByBarcode(int barcode) {
        Book book = null;

        String sql = "SELECT * FROM books WHERE barcode = ?";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement selectBook = connection.prepareStatement(sql)) {

            selectBook.setInt(1, barcode);

            ResultSet resultSet = selectBook.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

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
