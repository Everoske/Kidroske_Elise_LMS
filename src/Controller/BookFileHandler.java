package Controller;
import Model.Book;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Responsible for saving and loading data from the library-collection-data file.
 */
public class BookFileHandler {
    private Path collectionPath;
    private Charset encoding;

    public BookFileHandler(String collectionString) {
        this(collectionString, StandardCharsets.UTF_8);
    }

    public BookFileHandler(String collectionString, Charset encoding) {
        collectionPath = Paths.get(collectionString);
        this.encoding = encoding;
    }

    /*
    Given a Path object, returns a collection of books from a given file location
     */
    public ArrayList<Book> getBooksFromFile(Path path) {
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
    public ArrayList<Book> getBooksFromFile(String pathString) {
        Path path = Paths.get(pathString);
        return getBooksFromFile(path);
    }

    /*
    Returns the Library's Collection from the library-collection-data file
     */
    public ArrayList<Book> getLibraryCollection() {
        return getBooksFromFile(collectionPath);
    }

    /*
    Overwrites the library-collection-data file
     */
    public void overwriteCollection(Collection<Book> updatedCollection) {
        File oldCollectionData = collectionPath.toFile();

        // Delete old collection so it can be rewritten
        oldCollectionData.delete();

        // Write to a new library-collection-data file using the updated collection
        try (BufferedWriter bufferedWriter =
                Files.newBufferedWriter(collectionPath, encoding, StandardOpenOption.CREATE);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            for (Book book : updatedCollection) {
                printWriter.println(getBookString(book));
            }

        } catch (IOException exception) {
            System.out.println("\nUnable to overwrite file...");
        }
    }

    /*
    Checks to ensure the file is in the correct format using a regular expression
     */
    public boolean validateFormat(String line) {
        Pattern pattern = Pattern.compile("[0-9]+,[^,;]+,[^,;]+");
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

        try {
            bookId = Integer.parseInt(parameters[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        return new Book(bookId, parameters[1], parameters[2]);
    }

    /*
    Converts a Book object into its String representation
     */
    private String getBookString(Book book) {
        return book.getBookId() + "," + book.getTitle() + "," + book.getAuthor();
    }
}
