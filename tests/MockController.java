import Application.LibraryCore;
import Controller.IBookController;
import Domain.Book;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/07/2024
Name: Mock Controller
Description:
This class exists to test the behavioral requirements of the program
 */
public class MockController implements IBookController {
    private final LibraryCore libraryCore;

    public MockController(LibraryCore libraryCore) {
        this.libraryCore = libraryCore;
    }

    /*
    Name: Add Books From File
    Arguments: String file path
    Returns: int number of books added
    Description:
    Gets books from an absolute path supplied by the user
    and returns the number of books successfully added
     */
    public int addBooksFromFile(String path) {
        int numberBooksBefore = libraryCore.getLibraryBooks().size();
        libraryCore.getBooksFromTextFile(path, this);
        int numberBooksAfter = libraryCore.getLibraryBooks().size();
        return numberBooksAfter - numberBooksBefore;
    }

    /*
    Name: Remove Book By Title
    Arguments: String title
    Returns: int number of books removed
    Description:
    Attempts to remove a book from the library database given a title
    and returns the number of books removed
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

    /*
    Name: Remove Book By Barcode
    Arguments: String barcode
    Returns: int number of books removed
    Description:
    Attempts to remove a book from the library database given a barcode
    and returns the number of books removed
     */
    public int removeBookByBarcode(String barcode) {
        int numberBooksBefore = libraryCore.getLibraryBooks().size();
        Book book = libraryCore.findBookByBarcode(barcode);
        if (book == null) {
            return 0;
        }
        libraryCore.removeBookFromCollection(book, this);
        int numberBooksAfter = libraryCore.getLibraryBooks().size();

        return numberBooksBefore - numberBooksAfter;
    }

    /*
    Name: Check In Book
    Arguments: String title
    Returns: Book object updated from library database
    Description:
    Attempts to check in a Book in the library and
    returns the checked-in book
     */
    public Book checkInBook(String title) {
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            return null;
        }
        libraryCore.checkInBook(book, this);
        return libraryCore.findBookByTitle(title);
    }

    /*
    Name: Check Out Book
    Arguments: String title
    Returns: Book object updated from library database
    Description:
    Attempts to check out a Book in the library and
    returns the checked-out book
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

    /*
    Name: Invoke Preview
    Arguments: ArrayList<Book> representing new books from text file
    Returns: Void
    Description:
    Confirms the addition of new books to the library database
     */
    @Override
    public void invokePreview(ArrayList<Book> newBooks) {
        libraryCore.addBooksToDatabase(newBooks, this);
    }
}
