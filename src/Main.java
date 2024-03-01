import Application.LibraryCore;
import Test.TestController;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/01/2024
Name: Main
Description:
The purpose of this program is to allow users to manage the contents of a library.
The library collection is represented by Book objects stored in a relational database.
This program allows users to view the books in the library's database, add new books
by providing text files, delete existing books using their title or barcode, and
check out/check in books using their title.

This class is the insertion point for the program at its current state. It contains the
main components of the application as well as the application loop.
 */
public class Main {
    /*
    Name: Main
    Arguments: None
    Returns: Void
    Description:
    Contains the application loop and used to process and respond to user input
     */
    public static void main(String[] args) {
        boolean isRunning = true;
        Scanner userInputReader = new Scanner(System.in);
        LibraryCore libraryCore = new LibraryCore(StandardCharsets.UTF_8);
        TestController controller = new TestController(libraryCore, userInputReader);

        // Application loop
        while(isRunning) {
            // Show menu options and get user input
            controller.showOptions();
            int userInput = controller.getIntegerFromUser();

            switch (userInput) {
                // Print books to user
                case 1 -> {
                    controller.printCollection();
                    System.out.println();
                    controller.awaitEnterPress();
                }

                // Get books from a user provided path
                case 2 -> controller.getBooksFromPath();

                case 3 -> controller.checkOutBook();

                case 4 -> controller.checkInBook();

                // Remove a book by entering either its title or barcode
                case 5 -> controller.selectRemovalMethod();

                // Terminate program
                case 6 -> {
                    System.out.println("\nTerminating program...");
                    isRunning = false;
                }

                default -> System.out.println("\nInvalid number. Please enter a number between 1 and 4...\n");
            }
        }

        // Close Scanner before exiting
        userInputReader.close();
    }
}
