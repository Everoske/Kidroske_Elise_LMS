package application;

import controller.IBookController;
import database.DatabaseManager;
import database.ExternalSQLHandler;
import database.SQLForm;
import domain.Book;
import domain.BookStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the application layer of the library management system. Processes and
 * validates data from external files and passes them on for use. Handles exchanges
 * of information between the controller and database layers. Enforces business logic.
 * @author Elise Kidroske
 */
public class LibraryCore {

    private final DatabaseManager databaseManager;
    private final Charset encoding;

    public LibraryCore(Charset encoding) {
        this.encoding = encoding;
        databaseManager = new DatabaseManager();
    }

    /**
     * Retrieves Book objects from an external text file provided by the user.
     * @param pathString String path to a text file.
     * @param bookController IBookController representing the controller layer.
     */
    public void getBooksFromTextFile(String pathString, IBookController bookController) {
        ArrayList<Book> books = readBooks(pathString);

        if (books == null || books.isEmpty()) {
            bookController.invokeError("Unable to read any books from file.");
            return;
        }

        bookController.invokePreview(books);
    }

    /**
     * Retrieves Book objects from an external database provided by the user.
     * @param form SQLForm representing connection information for an external database.
     * @param bookController IBookController representing the controller layer.
     */
    public void getBooksFromDatabase(SQLForm form, IBookController bookController) {
        ExternalSQLHandler handler = new ExternalSQLHandler();

        ArrayList<Book> books = handler.getBooksFromDatabase(form);

        if (books == null || books.isEmpty()) {
            bookController.invokeError("Unable to read any books from database.");
            return;
        }

        bookController.invokePreview(books);
    }

    /**
     * Instructs the database layer to add Book objects to the library database.
     * @param newBooks Book List representing books to add to database.
     * @param bookController IBookController representing the controller layer.
     */
    public void addBooksToDatabase(List<Book> newBooks, IBookController bookController) {
        boolean insertedBooks = databaseManager.insertBooks(newBooks);

        if (!insertedBooks) {
            bookController.invokeError("Unable to insert books.");
            return;
        }

        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage("Books were added successfully.");
    }

    /**
     * Instructs the database layer to remove the provided Book from the library database.
     * @param book
     * @param bookController
     */
    public void removeBookFromCollection(Book book, IBookController bookController) {
        boolean bookDeleted = databaseManager.deleteBook(book);

        if (!bookDeleted) {
            bookController.invokeError("Unable to delete book.");
            return;
        }

        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully removed from the database.");
    }

    /**
     * Enforces check-out logic for Book objects, checks out Book objects if applicable,
     * and instructs the database layer to update the provided book in the library database.
     * @param book Book object to check out.
     * @param bookController IBookController representing the controller layer.
     */
    public void checkOutBook(Book book, IBookController bookController) {
        if (book.getBookStatus() == BookStatus.CHECKED_OUT) {
            bookController.invokeError("You cannot check out a book that is already checked out.");
            return;
        }

        book.setBookStatus(BookStatus.CHECKED_OUT);

        LocalDate dueDate = LocalDate.now().plusWeeks(4);
        book.setDueDate(dueDate.toString());

        boolean bookUpdated = databaseManager.updateBook(book);

        if (!bookUpdated) {
            bookController.invokeError("Unable to update book.");
            return;
        }

        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully checked out.\nThe new due date is " + book.getDueDate() + ".");
    }

    /**
     * Enforces check-in logic for Book objects, checks in Book objects if applicable,
     * and instructs the database layer to update the provided book in the library database.
     * @param book Book object to check out.
     * @param bookController IBookController representing the controller layer.
     */
    public void checkInBook(Book book, IBookController bookController) {
        if (book.getBookStatus() == BookStatus.CHECKED_IN) {
            bookController.invokeError("You cannot check in a book that is already checked in.");
            return;
        }

        book.setBookStatus(BookStatus.CHECKED_IN);
        book.setDueDate("");

        boolean bookUpdated = databaseManager.updateBook(book);

        if (!bookUpdated) {
            bookController.invokeError("Unable to update book.");
            return;
        }

        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully checked in.");
    }

    /**
     * Instructs the database layer to search for a Book object when
     * provided a title.
     * @param title String title to search for.
     * @return Book object that matches title provided.
     */
    public Book findBookByTitle(String title) {
        return databaseManager.searchByTitle(title);
    }

    /**
     * Instructs the database layer to search for a Book object when
     * provided a barcode.
     * @param barcode Int barcode to search for.
     * @return Book object that matches barcode provided.
     */
    public Book findBookByBarcode(int barcode) {
        return databaseManager.searchByBarcode(barcode);
    }

    /**
     * Instructs the database layer to retrieve all books from the library database.
     * @return Book ArrayList containing all books in library database.
     */
    public ArrayList<Book> getLibraryBooks() {
        return databaseManager.getAllBooks();
    }

    /**
     * Attempts to read Book objects from a provided text file.
     * @param path Path object representing absolute path to text file.
     * @return Book ArrayList containing books retrieved from the provided text file.
     */
    private ArrayList<Book> readBooks(Path path) {
        ArrayList<Book> books = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, encoding)) {

            String line = reader.readLine();

            while (line != null) {
                if (validateFormat(line)) {
                    Book book = createBookFromString(line);
                    if (book != null) {
                        books.add(book);
                    }
                }

                line = reader.readLine();
            }
        } catch (IOException ignored) {
        }

        return books;
    }

    /**
     * Attempts to read Book objects from a provided text file.
     * @param pathString String absolute path to text file.
     * @return Book ArrayList containing books retrieved from the provided text file.
     */
    public ArrayList<Book> readBooks(String pathString) {
        try {
            Path path = Paths.get(pathString);
            return readBooks(path);
        } catch (InvalidPathException ignored) {
        }

        return null;
    }

    /**
     * Ensures a file is in the correct format using a regular expression.
     * @param line String line of text from a text document.
     * @return True if validation is successful, false otherwise.
     */
    private boolean validateFormat(String line) {
        // This pattern matches title,author,genre,checked_status
        Pattern pattern = Pattern.compile("[^,;]+,[^,;]+,[^,;]+,[^,;]+,[^,;]*");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * Converts a String into a Book object.
     * @param bookString String line of text to be converted.
     * @return Book object from provided String.
     */
    private Book createBookFromString(String bookString) {

        String[] parameters = bookString.split(",");

        String title = parameters[0];
        String author = parameters[1];
        String genre = parameters[2];
        String status = parameters[3];

        BookStatus bookStatus = stringToBookStatus(status);

        if (bookStatus == null) {
            return null;
        }

        if (parameters.length > 4) {
            String dateString = parameters[4];

            LocalDate dueDate = parseLocalDate(dateString);
            if (dueDate != null) {
                return  new Book(-1, title, author, genre,  bookStatus, dateString);
            }
        }

        return new Book(-1,  title, author, genre,  bookStatus);
    }

    /**
     * Converts a String value into a BookStatus enum.
     * @param text String value to convert.
     * @return BookStatus processed.
     */
    private BookStatus stringToBookStatus(String text) {
        if (text.equalsIgnoreCase(BookStatus.CHECKED_OUT.toString()) ||
                text.equalsIgnoreCase(BookStatus.CHECKED_IN.toString())) {
            return BookStatus.valueOf(text.toUpperCase());
        }
        return null;
    }

    /**
     * Attempts to convert a String into a LocalData object.
     * @param dateString String date to parse.
     * @return LocalData representing processed date.
     */
    private LocalDate parseLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeException ignored) {

        }

        return null;
    }
}
