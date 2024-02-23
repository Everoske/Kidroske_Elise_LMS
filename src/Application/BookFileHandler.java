package Application;
import Domain.Book;
import Domain.BookStatus;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Responsible for reading lines from user-provided text files and returning Book objects
 */
public class BookFileHandler {
    private Charset encoding;

    public BookFileHandler(String collectionString) {
        this(StandardCharsets.UTF_8);
    }

    public BookFileHandler(Charset encoding) {
        this.encoding = encoding;
    }

    /*
    Given a Path object, returns a collection of books from a given file location
     */
    public ArrayList<Book> readBooks(Path path) {
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
    public boolean validateFormat(String line) {
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
        int barcode;
        BookStatus bookStatus = stringToBookStatus(parameters[5]);

        if (bookStatus == null) {
            return null;
        }

        try {
            bookId = Integer.parseInt(parameters[0]);
            barcode = Integer.parseInt(parameters[2]);
        } catch (NumberFormatException e) {
            return null;
        }

        return new Book(bookId, parameters[1], barcode, parameters[3], parameters[4], bookStatus);
    }

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
    private String getBookString(Book book) {
        return book.getBookId() + "," + book.getTitle() + "," + book.getAuthor();
    }
}
