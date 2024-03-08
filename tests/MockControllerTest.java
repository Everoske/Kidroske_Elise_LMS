import Application.LibraryCore;
import Domain.Book;
import Domain.BookStatus;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/07/2024
Name: Mock Controller Test
Description:
Tests the behavioral requirements of the application through
a mock controller implementation
 */
class MockControllerTest {

    MockController mockController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        LibraryCore libraryCore = new LibraryCore(StandardCharsets.UTF_8);
        mockController = new MockController(libraryCore);
    }

    /*
    Test Name: Add Books From Test File
    Purpose: Test ability to add books from a user-given text file
    Input: A text file with 4 parsable book objects
    Expected Outcome: int 4 representing books added
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Add Books From File Test")
    void addBooksFromFile() {
        String path = "./tests/test-books.txt";
        int expectedBooksAdded = 4;
        int actualBooksAdded = mockController.addBooksFromFile(path);

        assertEquals(expectedBooksAdded, actualBooksAdded);
    }

    /*
    Test Name: Remove Book by Title
    Purpose: Test ability to remove books from a user-provided book title
    Input: String title
    Expected Outcome: int 1 representing the number of books removed
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Remove Book by Title Test")
    void removeBookByTitle() {
        int expectedRemoved = 1;
        int actualRemoved = mockController.removeBookByTitle("Winter");

        assertEquals(expectedRemoved, actualRemoved);
    }

    /*
    Test Name: Remove Book by Barcode
    Purpose: Test ability to remove books from a user-provided book barcode
    Input: String barcode
    Expected Outcome: int 1 representing the number of books removed
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Remove Book by Barcode Test")
    void removeBookByBarcode() {
        int expectedRemoved = 1;
        int actualRemoved = mockController.removeBookByBarcode("17465643423");

        assertEquals(expectedRemoved, actualRemoved);
    }

    /*
    Test Name: Check In Book
    Purpose: Test ability to check in books from a user-provided book title
    Input: String title
    Expected Outcome: Book object returned is not null. Book status equals checked in.
    Book due date equals "null."
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Check In Book Test")
    void checkInBook() {
        Book book = mockController.checkInBook("Winter");

        assertNotNull(book);
        assertEquals(BookStatus.CHECKED_IN, book.getBookStatus());
        assertEquals("null", book.getDueDate());
    }

    /*
    Test Name: Check Out Book
    Purpose: Test ability to check out books from a user-provided book title
    Input: String title
    Expected Outcome: Book object returned is not null. Book status equals checked out.
    Book due date is equal to 4 weeks after today's date.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Check Out Book Test")
    void checkOutBook() {
        Book book = mockController.checkOutBook("Cress");

        assertNotNull(book);
        assertEquals(BookStatus.CHECKED_OUT, book.getBookStatus());

        // Find the date 4 weeks from today
        String expectedDate = LocalDate.now().plusWeeks(4).toString();
        assertEquals(expectedDate, book.getDueDate());
    }
}