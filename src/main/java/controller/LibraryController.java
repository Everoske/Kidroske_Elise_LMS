package controller;

import application.LibraryCore;
import domain.Book;
import javafx.scene.control.Label;
import presentation.ConfirmationDialog;
import presentation.MessageDialog;
import presentation.MessageType;
import presentation.PreviewBooksDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/24/2024
Name: Library Controller
Description:
The purpose of this class is to manage the user interface and process user interactions.
It implements IBookController, so it can manage the flow of information between the user and
the application layer.
 */
public class LibraryController implements IBookController {

    @FXML
    public ListView<Book> bookList;
    @FXML
    public TextField removeBookByTitle;
    @FXML
    public TextField removeBookByBarcode;
    @FXML
    public TextField checkBooksField;

    @FXML
    public Label bookTitleText;
    @FXML
    public Label bookBarcodeText;
    @FXML
    public Label bookAuthorText;
    @FXML
    public Label bookGenreText;
    @FXML
    public Label bookStatusText;
    @FXML
    public Label bookDueDateText;

    private LibraryCore libraryCore;
    private ObservableList<Book> observableBooks;

    private Book selectedBook;

    /*
    Name: Initialize
    Arguments: None
    Returns: Void
    Description:
    Initializes the application layer and handles setting up the UI and event listeners.
     */
    @FXML
    public void initialize() {
        libraryCore = new LibraryCore(StandardCharsets.UTF_8);

        // Initialize the Observable Books List
        ArrayList<Book> books = new ArrayList<>();
        observableBooks = FXCollections.observableList(books);

        // Set the Book Cell Factory for the Book List View
        bookList.setCellFactory(new BookCellFactory());
        // If a Book is selected, display its information. Otherwise, clear any book information and set
        // selected Book to null
        bookList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldBook, newBook) -> {
            if (newBook != null) {
                selectedBook = newBook;
                setBookInformation(newBook);
            } else {
                clearBookInformation();
                selectedBook = null;
            }
        });
        // Initialize the Book List View with the observable book list
        bookList.getItems().addAll(observableBooks);

        // Set Text Change listeners
        removeBookByTitle.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // If the new value is not empty, but the old value is empty, clear contents of
            // the remove book by barcode field
            if (oldValue.trim().isEmpty() || !newValue.trim().isEmpty()) {
                removeBookByBarcode.setText("");
                clearSelection();
            }
        });

        removeBookByBarcode.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // If the new value is not empty, but the old value is empty, clear contents of
            // the remove book by title field
            if (oldValue.trim().isEmpty() || !newValue.trim().isEmpty()) {
                removeBookByTitle.setText("");
                clearSelection();
            }
        });

        // Clear selected book if something is typed into the check book field
        checkBooksField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearSelection();
            }
        }));
    }

    /*
    Name: On Remove Book
    Arguments: ActionEvent on click event
    Returns: Void
    Description:
    Responds to user's request to delete book. Evaluates the information provided.
    If the proper information is provided, it passes the request to the application layer.
    Otherwise, it informs the user that information is missing.
     */
    public void onRemoveBook(ActionEvent event) {
        Book book;

        // If a Book is selected in the Book List View, attempt to delete it
        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = removeBookByTitle.getText().trim();
            String bookBarcode = removeBookByBarcode.getText().trim();

            // Check if user provided a barcode or title
            // Attempt to grab a book from the database
            if (!bookTitle.isEmpty()) {
                book = libraryCore.findBookByTitle(bookTitle);
            } else if (!bookBarcode.isEmpty()) {
                book = libraryCore.findBookByBarcode(bookBarcode);
            } else {
                // No book was provided
                return;
            }
        }

        // Book does not exist, inform the user
        if (book == null) {
            this.invokeError("This book does not match any books in the library's collection.");
            return;
        }

        // Ask the user for confirmation to delete the book
        this.invokeDeleteConfirmation("Are you sure you want to delete " +
                book.getTitle() + "\nfrom the library", book);
    }

    /*
    Name: On Check Out Books
    Arguments: ActionEvent on click event
    Returns: Void
    Description:
    Processes a user's request to check out a book
     */
    public void onCheckOutBooks(ActionEvent event) {
        Book book;

        // If a Book is selected in the Book List View, attempt to delete it
        if (selectedBook != null) {
            book = selectedBook;
        } else {
            // Check if user provided a title and attempt to grab it from database
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                // No book was provided
                return;
            }

            book = libraryCore.findBookByTitle(bookTitle);
        }

        // Book does not exist, inform the user
        if (book == null) {
            this.invokeError("This book does not match any books in the library's collection.");
            return;
        }

        // Pass of the request to the application layer
        libraryCore.checkOutBook(book, this);
    }

    /*
    Name: On Check In Books
    Arguments: ActionEvent on click event
    Returns: Void
    Description:
    Processes a user's request to check in a book
     */
    public void onCheckInBooks(ActionEvent event) {
        Book book;

        // If a Book is selected in the Book List View, attempt to delete it
        if (selectedBook != null) {
            book = selectedBook;
        } else {
            // Check if user provided a title and attempt to grab it from database
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                // No book was provided
                return;
            }

            book = libraryCore.findBookByTitle(bookTitle);

            // Book does not exist, inform the user
            if (book == null) {
                this.invokeError("The title you entered does not match any books in the library's collection.");
                return;
            }
        }

        // Pass of the request to the application layer
        libraryCore.checkInBook(book, this);
    }

    /*
    Name: On Add Books From File Click
    Arguments: ActionEvent on click event
    Returns: Void
    Description:
    Prompts the user to provide a text file using a file chooser. Tells application layer
    to process the provided file for book objects.
     */
    public void onAddBooksFromFileClick(ActionEvent event) {
        // Open a file chooser that exclusively looks for text files
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Tell the application layer to attempt to read from these files
            libraryCore.getBooksFromTextFile(selectedFile.getAbsolutePath(), this);
        }
    }

    // TODO: Implement External Database Access
    public void onAddBooksFromDatabaseClick(ActionEvent event) {
    }

    /*
    Name: On Display Books
    Arguments: ActionEvent on click event
    Returns: Void
    Description:
    Display the library's collection to the Book ListView
     */
    public void onDisplayBooks(ActionEvent event) {
        // Get books via application layer
        ArrayList<Book> books = libraryCore.getLibraryBooks();
        // Clear observable books and add the updated books from the database
        observableBooks.clear();
        observableBooks.addAll(books);

        // Set items in Book List View
        bookList.setItems(observableBooks);
    }

    /*
    Name: Set Book Information
    Arguments: Book book to display
    Returns: Void
    Description:
    Displays a book's information in the user interface.
     */
    private void setBookInformation(Book book) {
        bookTitleText.setText(book.getTitle());
        bookBarcodeText.setText(book.getBarcode());
        bookAuthorText.setText(book.getAuthor());
        bookGenreText.setText(book.getGenre());
        bookStatusText.setText(book.getBookStatus().toString());
        bookDueDateText.setText(book.getDueDate());
    }

    /*
    Name: Clear Book Information
    Arguments: None
    Returns: Void
    Description:
    Removes all book information.
     */
    private void clearBookInformation() {
        bookTitleText.setText("None");
        bookBarcodeText.setText("None");
        bookAuthorText.setText("None");
        bookGenreText.setText("None");
        bookStatusText.setText("None");
        bookDueDateText.setText("None");
    }

    /*
    Name: Clear Selection
    Arguments: None
    Returns: Void
    Description:
    Clears the selected book.
     */
    private void clearSelection() {
        // Deselect book from Book List View and set Selected Book to null
        bookList.getSelectionModel().clearSelection();
        selectedBook = null;
    }

    /*
    Name: Invoke Message
    Arguments: String message
    Returns: Void
    Description:
    Allows other classes to send a message to the user.
     */
    @Override
    public void invokeMessage(String message) {
        MessageDialog messageDialog = new MessageDialog(message, MessageType.INFORMATIVE);
        messageDialog.show();
    }

    /*
    Name: Invoke Error
    Arguments: String message
    Returns: Void
    Description:
    Allows other classes to send an error message to the user.
     */
    @Override
    public void invokeError(String message) {
        MessageDialog errorDialog = new MessageDialog(message, MessageType.ERROR);
        errorDialog.show();
    }

    /*
    Name: Invoke Delete Confirmation
    Arguments: String message, Book to delete
    Returns: Void
    Description:
    Asks the user for confirmation before deleting a book from the database.
     */
    @Override
    public void invokeDeleteConfirmation(String message, Book book) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(message);
        confirmationDialog.showAndWait();

        ButtonType result = confirmationDialog.getResult();

        if (result != null && result.equals(ButtonType.OK)) {
            libraryCore.removeBookFromCollection(book, this);
        }
    }

    /*
    Name: Update Content
    Arguments: ArrayList<Book> representing library collection
    Returns: Void
    Description:
    Updates the list with the updated contents of the database.
    In the final product, this list will be an observable list.
     */
    @Override
    public void updateContent(ArrayList<Book> updatedBooks) {
        // Attempt to store selected book's id
        int selectedId = -1;
        if (selectedBook != null) {
            selectedId = selectedBook.getBookId();
        }

        // Clear and update observable list
        observableBooks.clear();
        observableBooks.addAll(updatedBooks);

        // If selected book id was caught, find it in updated books and display it
        if (selectedId > -1) {
            for (Book book : observableBooks) {
                if (selectedId == book.getBookId()) {
                    int index = bookList.getItems().indexOf(book);
                    bookList.getSelectionModel().select(index);
                    bookList.getFocusModel().focus(index);
                    selectedBook = bookList.getItems().get(index);
                    return;
                }
            }
        }
    }

    /*
    Name: Invoke Preview
    Arguments: ArrayList<Book> representing new books from text file
    Returns: Void
    Description:
    Shows the user books from a selected source and asks for
    confirmation before adding the books to the database.
    */
    @Override
    public void invokePreview(ArrayList<Book> newBooks) {
        PreviewBooksDialog previewBooks = new PreviewBooksDialog(newBooks);
        previewBooks.showAndWait();

        ButtonType result = previewBooks.getResult();
        if (result != null && result.equals(ButtonType.OK)) {
            libraryCore.addBooksToDatabase(newBooks, this);
        }
    }
}