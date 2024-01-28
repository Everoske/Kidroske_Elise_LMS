import Controller.LibraryManager;

import java.util.Scanner;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
This program is a console-based library management system. It is meant to
be used to manage books in a library's collection. It contains methods for saving books,
removing books, and viewing books in the collection. The collection is preserved on a
text file which is read from and written to by the application.
 */

public class Main {
    private static Scanner userInputReader;
    private static LibraryManager libraryManager;

    /*
    Contains the application loop and used to process and respond to user input
     */
    public static void main(String[] args) {
        boolean isRunning = true;
        userInputReader = new Scanner(System.in);
        libraryManager = new LibraryManager();

        // Application loop
        while(isRunning) {
            printOptions();

            int processedResponse = getIntegerFromUser();

            switch (processedResponse) {
                case 1 -> {
                    printCollection();
                }
                case 2 -> {
                    addBooksFromPath();
                }
                case 3 -> {
                    removeBook();
                }
                case 4 -> {
                    // Terminate program
                    System.out.println("\nTerminating program...");
                    isRunning = false;
                }
                default -> {
                    System.out.println("\nInvalid number. Please enter a number between 1 and 4...\n");
                    awaitEnterPress();
                }
            }
        }

        userInputReader.close();
    }

    /*
    This prints the user's options for interacting with the library
     */
    public static void printOptions() {
        System.out.println("--- LIBRARY MANAGEMENT SYSTEM ---");
        System.out.println("Please select one of the following options:");
        System.out.println("1. Show all books");
        System.out.println("2. Add books from a file");
        System.out.println("3. Remove books from the collection");
        System.out.println("4. Quit\n");
    }

    /*
    Prints the library collection
     */
    private static void printCollection() {
        System.out.println();
        libraryManager.printCollection();
        System.out.println();
        awaitEnterPress();
    }

    /*
    Adds books from absolute path entered by the user
     */
    private static void addBooksFromPath() {
        System.out.println("\nEnter the absolute path of the file you wish to load from:");
        String absolutePath = userInputReader.nextLine();
        if (libraryManager.addBooksFromFile(absolutePath)) {
            System.out.println("\nLoaded books successfully\n");
        } else {
            System.out.println("\nUnable to add books...\n");
        }
        awaitEnterPress();
    }

    /*
    Removes book based off user input
     */
    private static void removeBook() {
        System.out.println("\nPlease select a book to delete:\n");
        int bookId = getIntegerFromUser();
        if (libraryManager.removeBook(bookId)) {
            System.out.println("\nBook deleted successfully\n");
        } else {
            System.out.println("\nUnable to delete book: Book does not exist\n");
        }
        awaitEnterPress();
    }

    /*
    Waits for a user to press enter
     */
    public static void awaitEnterPress() {
        System.out.print("Press enter to continue... ");
        userInputReader.nextLine();
        System.out.println();
    }

    /*
    Gets an integer from the user
    Loops until an integer value is given
     */
    public static int getIntegerFromUser() {
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
}
