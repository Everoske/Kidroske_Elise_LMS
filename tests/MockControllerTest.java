import application.LibraryCore;
import domain.Book;
import domain.BookStatus;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behavioral requirements of the application through
 * a mock controller implementation.
 * @author Elise Kidroske
 */
class MockControllerTest {

    MockController mockController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        LibraryCore libraryCore = new LibraryCore(StandardCharsets.UTF_8);
        mockController = new MockController(libraryCore);
    }

    /**
     * Test ability to add books from a user-given text file
     * @result Actual number of books added will equal the expected number of books added
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Add Books From File Test")
    void addBooksFromFile() {
        String path = "./tests/test-books.txt";
        int expectedBooksAdded = 4;
        int actualBooksAdded = mockController.addBooksFromFile(path);

        assertEquals(expectedBooksAdded, actualBooksAdded);
    }

    /**
     * Test ability to remove books from a user-provided book title.
     * @result Actual number of books removed should equal the expected number of books removed
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Remove Book by Title Test")
    void removeBookByTitle() {
        int expectedRemoved = 1;
        int actualRemoved = mockController.removeBookByTitle("Winter");

        assertEquals(expectedRemoved, actualRemoved);
    }

    /**
     * Test ability to remove books from a user-provided book barcode
     * @result Actual number of books removed should equal the expected number of books removed
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Remove Book by Barcode Test")
    void removeBookByBarcode() {
        int expectedRemoved = 1;
        int actualRemoved = mockController.removeBookByBarcode("17465643423");

        assertEquals(expectedRemoved, actualRemoved);
    }

    /**
     * Test ability to check in books from a user-provided book title
     * @result Book object returned is not null. Book status equals checked in. Book due date equals empty.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("Check In Book Test")
    void checkInBook() {
        Book book = mockController.checkInBook("Winter");

        assertNotNull(book);
        assertEquals(BookStatus.CHECKED_IN, book.getBookStatus());
        assertEquals("", book.getDueDate());
    }

    /**
     * Test ability to check out books from a user-provided book title
     * @result Book object returned is not null. Book status equals checked out. Book due date is 4 weeks from current date.
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