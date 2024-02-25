package Application;

import Controller.IInformable;
import Domain.Book;
import Domain.BookStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Primary component of application layer. Processes and validates data from external files
and passes them on for use. Handles exchanges of information between the controller and
database layers. Enforces business logic.
 */
public class LibraryCore {
    private Charset encoding;

    public LibraryCore(Charset encoding) {
        this.encoding = encoding;
    }

    /*
    Gets Book objects from an external text file
     */
    public void getBooksFromText(Path absolutePath, IInformable informable) {

    }

    /*
    Adds Books to the library's collection database
     */
    public void addBooksToDatabase(List<Book> newBooks, IInformable informable) {

    }

    /*
    Removes a Book from the library's collection database
     */
    public void removeBookFromCollection(Book book, IInformable informable) {

    }

    /*
    Changes the status of the book to checked out
    Informs database to update book in the library's collection
     */
    public void checkOutBook(Book book, IInformable informable) {

    }


    /*
    Changes the status of the book to checked in
    Informs database to update book in the library's collection
     */
    public void checkInBook(Book book, IInformable informable) {

    }

    /*
    Returns all the books from the library's collection
     */
    public List<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();

        return books;
    }

    /*
    Given a Path object, returns a collection of books from a given file location
     */
    private ArrayList<Book> readBooks(Path path) {
        ArrayList<Book> books = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, encoding)) {
            String line = reader.readLine();

            while (line != null) {
                // Ensure formatting
                if (validateFormat(line)) {
                    Book book = createBookFromString(line);
                    if (book != null) {
                        books.add(book);
                    }
                }

                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println("\nUnable to read from file...");
        }

        return books;
    }

    /*
    Returns a collection of books from a given file location String
     */
    public ArrayList<Book> readBooks(String pathString) {
        Path path = Paths.get(pathString);
        return readBooks(path);
    }

    /*
    Checks to ensure the file is in the correct format using a regular expression
     */
    private boolean validateFormat(String line) {
        Pattern pattern = Pattern.compile("[0-9]+,[^,;]+,[0-9]+,[^,;]+,[^,;]+,[^,;]+,[^,;]*");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /*
   Processes a string representation of a book into a Book object.
   Returns null if a NumberFormatException occurs
    */
    private Book createBookFromString(String bookString) {
        String[] parameters = bookString.split(",");
        int bookId;
        BookStatus bookStatus = stringToBookStatus(parameters[5]);

        if (bookStatus == null) {
            return null;
        }

        try {
            bookId = Integer.parseInt(parameters[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        //TODO: Convert Date into Date object if it exists

        // System.out.println(parameters[6]);

        return new Book(bookId, parameters[1], parameters[2], parameters[3], parameters[4], bookStatus);
    }

    /*
    Converts a String value into a BookStatus enum
     */
    private BookStatus stringToBookStatus(String text) {
        if (text.equalsIgnoreCase(BookStatus.CHECKED_OUT.toString()) ||
                text.equalsIgnoreCase(BookStatus.CHECKED_IN.toString())) {
            return BookStatus.valueOf(text.toUpperCase());
        }
        return null;
    }

    /*
    Converts a Book object into its String representation
     */
    public String getBookString(Book book) {
        return book.getBookId() + "," + book.getTitle() + ","
                + book.getBarcode() + "," + book.getAuthor() + ","
                + book.getGenre() + "," + book.getBookStatus();
    }
}
