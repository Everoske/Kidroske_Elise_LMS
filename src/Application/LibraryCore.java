package Application;

import Controller.IBookController;
import Database.DatabaseManager;
import Domain.Book;
import Domain.BookStatus;

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

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/01/2024
Name: Library Core
Description:
Primary component of the application layer. Processes and validates data from external files
and passes them on for use. Handles exchanges of information between the controller and
database layers. Enforces business logic.
 */
public class LibraryCore {

    private final DatabaseManager databaseManager;
    private final Charset encoding;

    public LibraryCore(Charset encoding) {
        this.encoding = encoding;
        databaseManager = new DatabaseManager();
    }

    /*
    Name: Get Books From Text File
    Arguments: String representing path to a text file, IBookController representing controller
    Returns: Void
    Description:
    Gets Book objects from an external text file
     */
    public void getBooksFromTextFile(String pathString, IBookController informable) {
        // Read books from a user provided location
        ArrayList<Book> books = readBooks(pathString);

        // If no books are processed, inform Controller and return
        if (books == null || books.isEmpty()) {
            informable.invokeError("Unable to read any books from file.");
            return;
        }

        // Send books to the Controller for preview
        informable.invokePreview(books);
    }

    /*
    Name: Add Books to Database
    Arguments: List<Book> representing books to add, IBookController representing controller
    Returns: Void
    Description:
    Adds Books to the library's collection database
     */
    public void addBooksToDatabase(List<Book> newBooks, IBookController bookController) {
        // Inform database manager to insert books to collection
        boolean insertedBooks = databaseManager.insertBooks(newBooks);

        // If insertion was unsuccessful, inform Controller and return
        if (!insertedBooks) {
            bookController.invokeError("Unable to insert books.");
            return;
        }

        // Inform Controller about the change
        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage("Books were added successfully.");
    }

    /*
    Name: Remove Book From Collection
    Arguments: Book for the book to remove, IBookController representing controller
    Returns: Void
    Description:
    Removes a Book from the library's collection database
     */
    public void removeBookFromCollection(Book book, IBookController bookController) {
        // Delete the book from the database
        boolean bookDeleted = databaseManager.deleteBook(book);

        // If the book cannot be deleted, invoke an error message and terminate process
        if (!bookDeleted) {
            bookController.invokeError("Unable to delete book.");
            return;
        }

        // Inform Controller layer about change
        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully removed from the database.");
    }

    /*
    Name: Check Out Book
    Arguments: Book for the book to check out, IBookController for the controller
    Returns: Void
    Description:
    Changes the status of the book to checked out
    Informs database to update book in the library's collection
     */
    public void checkOutBook(Book book, IBookController bookController) {
        // If book is already checked in, invoke an error message and terminate process
        if (book.getBookStatus() == BookStatus.CHECKED_OUT) {
            bookController.invokeError("You cannot check out a book that is already checked out.");
            return;
        }

        // Change Status
        book.setBookStatus(BookStatus.CHECKED_OUT);

        // Change the due date to be 4 weeks from the current date
        LocalDate dueDate = LocalDate.now();
        book.setDueDate(dueDate.plusWeeks(4).toString());

        // Tell Database layer to update book
        boolean bookUpdated = databaseManager.updateBook(book);

        // If there was an error with updating the book, invoke an error message and terminate process
        if (!bookUpdated) {
            bookController.invokeError("Unable to update book.");
            return;
        }

        // Inform Controller layer about the change
        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully checked out. The new due date is " + book.getDueDate() + ".");
    }

    /*
    Name: Check In Book
    Arguments: Book for the book to check in, IBookController for the controller
    Returns: Void
    Description:
    Changes the status of the book to checked in
    Informs database to update book in the library's collection
     */
    public void checkInBook(Book book, IBookController bookController) {
        // If book is already checked in, invoke an error message and terminate process
        if (book.getBookStatus() == BookStatus.CHECKED_IN) {
            bookController.invokeError("You cannot check in a book that is already checked in.");
            return;
        }

        // Change Status and set due date to a string literal null
        book.setBookStatus(BookStatus.CHECKED_IN);
        book.setDueDate("null");

        // Tell Database layer to update book
        boolean bookUpdated = databaseManager.updateBook(book);

        // If there was an error with updating the book, invoke an error message and terminate process
        if (!bookUpdated) {
            bookController.invokeError("Unable to update book.");
            return;
        }

        // Inform Controller layer about the change
        bookController.updateContent(databaseManager.getAllBooks());
        bookController.invokeMessage(book.getTitle() + " was successfully checked in.");
    }

    /*
    Name: Find Book By Title
    Arguments: String title to lookup
    Returns: Book
    Description:
    Searches for a book using its title
     */
    public Book findBookByTitle(String title) {
        return databaseManager.searchByTitle(title);
    }

    /*
    Name: Find Book By Barcode
    Arguments: String barcode to lookup
    Returns: Book
    Description:
    Searches for a book using its barcode
     */
    public Book findBookByBarcode(String barcode) {
        return databaseManager.searchByBarcode(barcode);
    }

    /*
    Name: Get Library Books
    Arguments: None
    Returns: ArrayList<Book> for the books from the library's database
    Description:
    Returns all the books from the library's collection
     */
    public ArrayList<Book> getLibraryBooks() {
        return databaseManager.getAllBooks();
    }

    /*
    Name: Read Books
    Arguments: Path for the path to a text file
    Returns: ArrayList<Book> for the books read from the text file
    Description:
    Given a Path object, returns a collection of books from a given file location
     */
    private ArrayList<Book> readBooks(Path path) {
        ArrayList<Book> books = new ArrayList<>();

        // Open the path with a Buffered Reader
        try (BufferedReader reader = Files.newBufferedReader(path, encoding)) {
            // Read the first line returned
            String line = reader.readLine();

            while (line != null) {
                // Ensure formatting
                if (validateFormat(line)) {
                    // Create a book object using the line returned
                    Book book = createBookFromString(line);
                    if (book != null) {
                        books.add(book);
                    }
                }

                // Continue reading through the entire document
                line = reader.readLine();
            }
        } catch (IOException ignored) {

        }

        return books;
    }

    /*
    Name: Read Books
    Arguments: String path
    Returns: ArrayList<Book> for the books read from the text file
    Description:
    Returns a collection of books from a given file location String
    Returns null if unable to generate a Path object from the given String
     */
    public ArrayList<Book> readBooks(String pathString) {
        // Attempt to create a Path object
        try {
            Path path = Paths.get(pathString);
            // Return books read using the overloaded method
            return readBooks(path);
        } catch (InvalidPathException ignored) {

        }

        return null;
    }

    /*
    Name: Validate Format
    Arguments: String for a line in a text file
    Returns: True or false based on whether the line has a match to the regex
    Description:
    Checks to ensure the file is in the correct format using a regular expression
     */
    private boolean validateFormat(String line) {
        // This pattern matches title,barcode,author,genre,checked_status
        Pattern pattern = Pattern.compile("[^,;]+,[0-9]+,[^,;]+,[^,;]+,[^,;]+,[^,;]*");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /*
    Name: Create Book From String
    Argument: String for the book string
    Returns: Book
    Description:
    Processes a String representation of a book into a Book object.
    Returns null if unable to create a Book object
    The book ID is -1 which is meant to be a placeholder for database insertion
    */
    private Book createBookFromString(String bookString) {
        // Split string using commas as the delimiter
        String[] parameters = bookString.split(",");
        // Convert the string book status to the enum book status
        BookStatus bookStatus = stringToBookStatus(parameters[4]);

        // Books must have a Book Status
        if (bookStatus == null) {
            return null;
        }

        // Some books will have a due date, check if it exists
        if (parameters.length > 5) {
            String dateString = parameters[5];

            // Ensure the string provided can be converted into a LocalDate object
            LocalDate dueDate = parseLocalDate(dateString);
            if (dueDate != null) {
                // Returns a book with a due date
                return  new Book(-1, parameters[0], parameters[1], parameters[2], parameters[3], bookStatus, dateString);
            }
        }

        // Returns a book without a due date
        return new Book(-1, parameters[0], parameters[1], parameters[2], parameters[3], bookStatus);
    }

    /*
    Name: String to Book Status
    Arguments: String text
    Returns: Book Status enum
    Description:
    Converts a String value into a BookStatus enum
     */
    private BookStatus stringToBookStatus(String text) {
        // Ensure the text has an equivalent checked status regardless of case
        if (text.equalsIgnoreCase(BookStatus.CHECKED_OUT.toString()) ||
                text.equalsIgnoreCase(BookStatus.CHECKED_IN.toString())) {
            return BookStatus.valueOf(text.toUpperCase());
        }
        return null;
    }

    /*
    Name: Parse Local Date
    Arguments: String date
    Returns: Local Date
    Attempts to convert a String into a LocalDate object
    Returns null if no LocalDate can be translated
     */
    private LocalDate parseLocalDate(String dateString) {
        try {
            // Return the current date
            return LocalDate.parse(dateString);
        } catch (DateTimeException ignored) {

        }

        return null;
    }
}
