package Controller;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Responsible for managing the interaction between the user and the library's data
 */

import Model.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LibraryManager {
    public static final String COLLECTION_PATH = "./Data/library-collection-data.txt";
    private HashMap<Integer, Book> books;
    private BookFileHandler fileHandler;

    public LibraryManager() {
        fileHandler = new BookFileHandler(COLLECTION_PATH);
        books = new HashMap<>();
        loadBooks();
    }

    /*
    Adds books from a file containing books
     */
    public boolean addBooksFromFile(String filePath) {
        ArrayList<Book> newBooks = fileHandler.getBooksFromFile(filePath);

        if (newBooks.size() == 0) {
            System.out.println("\nNo books to add...");
            return false;
        }

        // Refers to max id in collection
        int maxId = Collections.max(books.keySet());

        for (Book book : newBooks) {
            // If id already exists, increment the max id of the collection and assign it to the book
            if (books.containsKey(book.getBookId())) {
                maxId++;
                book.setBookId(maxId);
            }

            books.put(book.getBookId(), book);
        }

        // Update collection with new data
        updateCollection();

        return true;
    }

    /*
    Removes books from HashMap and updates the collection file
     */
    public boolean removeBook(int bookId) {
        if (!books.containsKey(bookId)) {
            return false;
        }

        books.remove(bookId);

        // Update collection to reflect changes
        updateCollection();

        return true;
    }

    /*
    Prints all books currently loaded into memory
     */
    public void printCollection() {
        for (Book book : books.values()) {
            System.out.println(book.getBookId() + ". Title: " + book.getTitle() + " | Author: " + book.getAuthor());
        }
    }

    /*
    Loads books from the library-collection file
     */
    private void loadBooks() {
        ArrayList<Book> booksList = fileHandler.getLibraryCollection();
        for (Book book : booksList) {
            books.putIfAbsent(book.getBookId(), book);
        }
    }

    /*
    Overwrites books in the library-collection file using the current data
     */
    private void updateCollection() {
        fileHandler.overwriteCollection(books.values());
    }
}
