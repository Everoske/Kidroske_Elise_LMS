package database;

import database.BookColumns;
import domain.Book;
import domain.BookStatus;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is responsible for handling data retrieval operations
 * from external user-provided databases.
 * @author Elise Kidroske
 */
public class ExternalSQLHandler {
    /**
     * Retrieves all books from a user-provided MySQL database.
     * @param server String name of SQL server.
     * @param database String database name.
     * @param username String username.
     * @param password String password.
     * @param tableName String table name.
     * @return Book ArrayList containing all books from the database.
     */
    private ArrayList<Book> getBooksMySQL(String server, String database,
                                         String username, String password,
                                         String tableName) {

        ArrayList<Book> books = new ArrayList<>();

        String connectionString = "jdbc:mysql://" + server + "/" + database;

        String selectString = "SELECT * FROM " + tableName;

        try(Connection connection = DriverManager.getConnection(connectionString, username, password);
            PreparedStatement selectStatement = connection.prepareStatement(selectString)) {

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

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

    /**
     * Retrieves all books from a user-provided SQLite database.
     * @param path String absolute path to *.db file.
     * @param tableName String table name.
     * @return Book ArrayList containing all books from the database.
     */
    private ArrayList<Book> getBooksSQLite(String path, String tableName) {

        ArrayList<Book> books = new ArrayList<>();

        String connectionString = "jdbc:sqlite:" + path;

        String selectString = "SELECT * FROM " + tableName;

        try(Connection connection = DriverManager.getConnection(connectionString);
            PreparedStatement selectStatement = connection.prepareStatement(selectString)) {

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString(BookColumns.TITLE);
                String author = resultSet.getString(BookColumns.AUTHOR);
                String genre = resultSet.getString(BookColumns.GENRE);
                String bookStatusString = resultSet.getString(BookColumns.BOOK_STATUS);
                BookStatus bookStatus = BookStatus.valueOf(bookStatusString);
                String dueDate = resultSet.getString(BookColumns.DUE_DATE);

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

    /**
     * Processes an SQLForm object and returns books from either an MySQL or SQLite database
     * based on the information provided by the form.
     * @param form SQLForm with external database connection information.
     * @return Book ArrayList representing all books from an external database.
     */
    public ArrayList<Book> getBooksFromDatabase(SQLForm form) {
        if (form.getSourceType().equals(SQLSourceType.MYSQL)) {
            return getBooksMySQL(form.getDataSource(), form.getDatabaseName(),
                    form.getUsername(), form.getPassword(), form.getTableName());
        }
        return getBooksSQLite(form.getDatabaseFilePath(), form.getTableName());
    }
}
