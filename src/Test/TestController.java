package Test;

import Application.LibraryCore;
import Controller.IBookController;
import Domain.Book;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
This class only exists for testing. It is meant to
stand in for the actual user interface controller
 */
public class TestController implements IBookController {
    private final LibraryCore libraryCore;
    private final Scanner userInputReader;
    private ArrayList<Book> books;

    public TestController(LibraryCore libraryCore, Scanner userInputReader) {
        this.libraryCore = libraryCore;
        this.userInputReader = userInputReader;
    }

    /*
    This prints the user's options for interacting with the library
     */
    public void showOptions() {
        System.out.println("--- LIBRARY MANAGEMENT SYSTEM ---");
        System.out.println("Please select one of the following options:");
        System.out.println("1. Show all books");
        System.out.println("2. Add books from file");
        System.out.println("3. Check out book");
        System.out.println("4. Check in book");
        System.out.println("5. Remove books from the collection");
        System.out.println("6. Quit\n");
    }

    /*
    Prints the library collection
     */
    public void printCollection() {
        System.out.println();
        System.out.println("Printing is taking place...\n");
        if (books == null) {
            books = libraryCore.getLibraryBooks();
        }

        for (Book book : books) {
            System.out.println(getBookString(book));
        }
    }

    /*
    Gets books from an absolute path supplied by the user
     */
    public void getBooksFromPath() {
        System.out.print("\nEnter the absolute path of the file you wish to load from: ");
        String absolutePath = userInputReader.nextLine();
        System.out.println();

        libraryCore.getBooksFromTextFile(absolutePath, this);
    }

    /*
    Asks the user for a book title and attempts to check it out
     */
    public void checkOutBook() {
        System.out.println("\nPlease select a book to check out:\n");
        System.out.print("Enter a title: ");
        String title = userInputReader.nextLine();
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            this.invokeError("The title you entered does not match any books in the library's collection.");
            return;
        }

        libraryCore.checkOutBook(book, this);
    }

    /*
    Asks the user for a book title and attempts to check it in
     */
    public void checkInBook() {
        System.out.println("\nPlease select a book to check in:\n");
        System.out.print("Enter a title: ");
        String title = userInputReader.nextLine();
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            this.invokeError("The title you entered does not match any books in the library's collection.");
            return;
        }

        libraryCore.checkInBook(book, this);
    }

    /*
    Prompts the user to choose between removing a book by its title
    or removing a book by its barcode
     */
    public void selectRemovalMethod() {
        System.out.println("\nHow would you like to select a book to remove?");
        System.out.println("\nBy Title: [T]");
        System.out.println("\nBy Barcode: [B]\n");
        System.out.print("[T] | [B]: ");

        boolean validResponseGiven = false;

        while (!validResponseGiven) {
            String userInput = userInputReader.nextLine();
            char inputChar = userInput.toUpperCase().toCharArray()[0];

            switch(inputChar) {
                case 'T' -> {
                    removeBookByTitle();
                    validResponseGiven = true;
                }
                case 'B' -> {
                    removeBookByBarcode();
                    validResponseGiven = true;
                }
                default -> System.out.println("Enter T for title or B for barcode.\n");
            }
        }
    }

    /*
    Removes book based on a title provided by the user
     */
    private void removeBookByTitle() {
        System.out.println("\nSelect a book to delete:\n");
        System.out.print("Enter a title: ");
        String title = userInputReader.nextLine();
        Book book = libraryCore.findBookByTitle(title);
        if (book == null) {
            this.invokeError("The title you entered does not match any books in the library's collection.");
            return;
        }

        this.invokeDeleteConfirmation("Are you sure you want to delete " + book.getTitle() + " from the library?", book);
    }

    /*
    Removes book based on a barcode provided by the user
     */
    private void removeBookByBarcode() {
        System.out.println("\nSelect a book to delete:\n");
        System.out.print("Enter a barcode: ");
        String barcode = userInputReader.nextLine();
        Book book = libraryCore.findBookByBarcode(barcode);
        if (book == null) {
            this.invokeError("The barcode you entered does not match any books in the library's collection.");
            return;
        }

        this.invokeDeleteConfirmation("Are you sure you want to delete " + book.getTitle() + " from the library?", book);
    }

    /*
    Waits for a user to press enter
     */
    public void awaitEnterPress() {
        System.out.print("Press enter to continue... ");
        userInputReader.nextLine();
        System.out.println();
    }

    /*
    Gets an integer from the user
    Loops until an integer value is given
     */
    public int getIntegerFromUser() {
        int userInt = -1;
        boolean userEnteredInt = false;

        while (!userEnteredInt) {
            System.out.print("Enter a number: ");
            String unprocessedResponse = userInputReader.nextLine();
            try {
                userInt = Integer.parseInt(unprocessedResponse);
                userEnteredInt = true;
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a number...\n");
            }
        }

        return userInt;
    }

    /*
    Attempts to get a Yes or No response from the user
    It returns either Y or N as the user's response
     */
    private String getYesOrNoResponse() {
        String response = "";
        boolean validResponseGiven = false;

        // Loop until user enters Y or N
        while (!validResponseGiven) {
            System.out.print("[Y] | [N]: ");
            String userInput = userInputReader.nextLine();

            // Check if the first char is one of the possible options
            char inputChar = userInput.toUpperCase().toCharArray()[0];
            if (Objects.equals(inputChar, 'Y') || Objects.equals(inputChar, 'N')) {
                // If a valid response, convert it into a string and break the loop
                response = String.valueOf(inputChar);
                validResponseGiven = true;
            } else {
                System.out.println("Enter Y for yes or N for no.\n");
            }
        }

        System.out.println();
        return response;
    }

    /*
    Converts a Book object into its String representation
     */
    public String getBookString(Book book) {
        return book.getTitle() + ","
                + book.getBarcode() + "," + book.getAuthor() + ","
                + book.getGenre() + "," + book.getBookStatus() + ","
                + book.getDueDate();
    }

    /*
    Allows other classes to send a message to the user
     */
    @Override
    public void invokeMessage(String message) {
        System.out.println("\n" + message + "\n");
        awaitEnterPress();
    }

    /*
    Allows other classes to send an error message to the user
     */
    @Override
    public void invokeError(String message) {
        System.out.println("\n" + message + "\n");
        awaitEnterPress();
    }

    /*
    Asks the user for confirmation before deleting a book from the database
     */
    @Override
    public void invokeDeleteConfirmation(String message, Book book) {
        System.out.println("\nAre you sure you want to remove " + book.getTitle() + " from the library?\n");
        String userResponse = getYesOrNoResponse();

        if (Objects.equals(userResponse, "Y")) {
            System.out.println("\nAttempting to remove book from database...");
            libraryCore.removeBookFromCollection(book, this);
        } else {
            System.out.println("\nOperation cancelled.\n");
            awaitEnterPress();
        }
    }

    /*
    Updates the list with the updated contents of the database
    In the final product, this list will be an observable list
     */
    @Override
    public void updateContent(ArrayList<Book> updatedBooks) {
        books = updatedBooks;
        printCollection();
    }

    /*
    Shows the user books from a selected source and asks for
    confirmation before adding the books to the database
     */
    @Override
    public void invokePreview(ArrayList<Book> newBooks) {
        for (Book book : newBooks) {
            System.out.println(getBookString(book));
        }

        System.out.println("\nAre you sure you want to add these books?\n");
        String userResponse = getYesOrNoResponse();

        if (Objects.equals(userResponse, "Y")) {
            System.out.println("Attempting to add books to database...");
            libraryCore.addBooksToDatabase(newBooks, this);
        } else {
            System.out.println("Operation cancelled.\n");
            awaitEnterPress();
        }
    }
}
