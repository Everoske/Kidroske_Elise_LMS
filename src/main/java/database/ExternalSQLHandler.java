package database;

import database.BookColumns;
import domain.Book;
import domain.BookStatus;

import java.sql.*;
import java.util.ArrayList;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 04/07/2024
Name: External SQL Handler
Description:
Responsible for handling data retrieval operations from external
user-provided databases. Requires the user of an SQL Form to read
books from a database
 */
public class ExternalSQLHandler {
    /*
    Name: Get Books MySql
    Arguments: String server, String database, String username, String password, String table name
    Returns: ArrayList<Book> representing all books from an external MySQL Database
    Description:
    Retrieves all books from a user-provided MySQL database
     */
    private ArrayList<Book> getBooksMySQL(String server, String database,
                                         String username, String password,
                                         String tableName) {

        ArrayList<Book> books = new ArrayList<>();

        // Create connection string to external MySQL database
        String connectionString = "jdbc:mysql://" + server + "/" + database;

        // Initialize a query string for all books in the database
        String selectString = "SELECT * FROM " + tableName;

        // Open a connection to the library database and create a statement using
        // try with resources
        try(Connection connection = DriverManager.getConnection(connectionString, username, password);
            PreparedStatement selectStatement = connection.prepareStatement(selectString)) {

            // Find all books in the database
            ResultSet resultSet = selectStatement.executeQuery();

            // Create book objects for each row of the books table
            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

                // Set the due date value
                if (dueDate != null) {
                    books.add(new Book(-1, title, author, genre, bookStatus, dueDate));
                } else {
                    books.add(new Book(-1, title, author, genre, bookStatus));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    /*
    Name: Get Books from an external SQLite database
    Arguments: String path, String tableName
    Returns: ArrayList<Book> representing all books from an external MySQL Database
    Description:
    Retrieves all books from a user-provided MySQL database
     */
    private ArrayList<Book> getBooksSQLite(String path, String tableName) {

        ArrayList<Book> books = new ArrayList<>();

        // Insert the user-provided table into the query
        String connectionString = "jdbc:sqlite:" + path;

        // Initialize a query string for all books in the database
        String selectString = "SELECT * FROM " + tableName;


        // Open a connection to the library database and create a statement using
        // try with resources
        try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement selectStatement = connection.prepareStatement(selectString)) {

            // Find all books in the database
            ResultSet resultSet = selectStatement.executeQuery();

            // Create book objects for each row of the books table
            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

                // Set the due date value
                if (dueDate != null) {
                    books.add(new Book(-1, title, author, genre, bookStatus, dueDate));
                } else {
                    books.add(new Book(-1, title, author, genre, bookStatus));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    /*
    Name: Get Books From Database
    Arguments: SQLForm form with connection information
    Returns: ArrayList<Book> representing all books from an external database
    Description:
    Processes the SQLForm and either returns books from a MySQL database or an SQLite database
     */
    public ArrayList<Book> getBooksFromDatabase(SQLForm form) {
        if (form.getSourceType().equals(SQLSourceType.MYSQL)) {
            return getBooksMySQL(form.getDataSource(), form.getDatabaseName(),
                    form.getUsername(), form.getPassword(), form.getTableName());
        }
        return getBooksSQLite(form.getDatabaseFilePath(), form.getTableName());
    }
}
