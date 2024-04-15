import application.LibraryCore;
import controller.IBookController;
import domain.Book;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This class is a mock controller implementation used for testing the
 * functional requirements of the library management system.
 * @author Elise Kidroske
 */
public class MockController implements IBookController {
    private final LibraryCore libraryCore;

    public MockController(LibraryCore libraryCore) {
        this.libraryCore = libraryCore;
    }

    /**
     * Attempts to add books to the database from an
     * external text file.
     * @param path String absolute path to a text file.
     * @return Int number of rows affected.
     */
    public int addBooksFromFile(String path) {
        int numberBooksBefore = libraryCore.getLibraryBooks().size();
        libraryCore.getBooksFromTextFile(path, this);
        int numberBooksAfter = libraryCore.getLibraryBooks().size();
        return numberBooksAfter - numberBooksBefore;
    }

    /**
     * Attempts to remove a book from the library database given a
     * title.
     * @param title String title of book to remove.
     * @return Int number of rows affected.
     */
    public int removeBookByTitle(String title) {
        int numberBooksBefore = libraryCore.getLibraryBooks().size();
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            return 0;
        }
        libraryCore.removeBookFromCollection(book, this);
        int numberBooksAfter = libraryCore.getLibraryBooks().size();

        return numberBooksBefore - numberBooksAfter;
    }

    /**
     * Attempts to remove a book from the library database given a
     * barcode.
     * @param barcode Int barcode of book to remove.
     * @return Int number of rows affected.
     */
    public int removeBookByBarcode(String barcode) {
        int numberBooksBefore = libraryCore.getLibraryBooks().size();
        Book book;

        try {
            int barcodeNumber = Integer.parseInt(barcode);
            book = libraryCore.findBookByBarcode(barcodeNumber);
        } catch (NumberFormatException e) {
            return 0;
        }

        if (book == null) {
            return 0;
        }
        libraryCore.removeBookFromCollection(book, this);
        int numberBooksAfter = libraryCore.getLibraryBooks().size();

        return numberBooksBefore - numberBooksAfter;
    }

    /**
     * Attempts to check in a book and update its
     * checked state in the database.
     * @param title String title of book to check in.
     * @return Int number of rows affected.
     */
    public Book checkInBook(String title) {
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            return null;
        }
        libraryCore.checkInBook(book, this);
        return libraryCore.findBookByTitle(title);
    }

    /**
     * Attempts to check out a book and update its
     * checked state in the database.
     * @param title String title of book to check out.
     * @return Int number of rows affected.
     */
    public Book checkOutBook(String title) {
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            return null;
        }
        libraryCore.checkOutBook(book, this);
        return libraryCore.findBookByTitle(title);
    }

    @Override
    public void invokeMessage(String message) {
    }

    @Override
    public void invokeError(String message) {
    }

    @Override
    public void invokeDeleteConfirmation(String message, Book book) {
    }

    @Override
    public void updateContent(ArrayList<Book> updatedBooks) {
    }

    /**
     * Adds new books to the library database.
     * @param newBooks Book ArrayList representing new books to add to library.
     */
    @Override
    public void invokePreview(ArrayList<Book> newBooks) {
        libraryCore.addBooksToDatabase(newBooks, this);
    }
}
