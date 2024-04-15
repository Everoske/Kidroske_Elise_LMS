package controller;

import application.LibraryCore;
import database.SQLForm;
import domain.Book;
import javafx.scene.control.Label;
import presentation.*;
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

/**
 * This class processes and responds to user interaction with the user interface.
 * This class manages the flow of information between the user and the application
 * layer.
 * @author Elise Kidroske
 */
public class LibraryController implements IBookController {

    /** Displays books from the library database to the user.*/
    @FXML
    public ListView<Book> bookList;
    /** TextField for entering a book title for removal.*/
    @FXML
    public TextField removeBookByTitle;
    /** TextField for entering a book barcode for removal.*/
    @FXML
    public TextField removeBookByBarcode;
    /** TextField for entering a book title to check in or check out.*/
    @FXML
    public TextField checkBooksField;
    /** Displays the title of the selected book.*/
    @FXML
    public Label bookTitleText;
    /** Displays the barcode of the selected book.*/
    @FXML
    public Label bookBarcodeText;
    /** Displays the author of the selected book.*/
    @FXML
    public Label bookAuthorText;
    /** Displays the genre of the selected book.*/
    @FXML
    public Label bookGenreText;
    /** Displays the status of the selected book.*/
    @FXML
    public Label bookStatusText;
    /** Displays the due date of the selected book.*/
    @FXML
    public Label bookDueDateText;

    /** Represents the application layer. Enforces business logic, processes
     * exchanges of external data, and communicates with the database layer.*/
    private LibraryCore libraryCore;
    /** Stores Book objects from the library database for display in the ListView.*/
    private ObservableList<Book> observableBooks;

    /** Represents the selected book in the ListView.*/
    private Book selectedBook;

    /**
     * Initializes the application layer and the user interface components.
     */
    @FXML
    public void initialize() {
        libraryCore = new LibraryCore(StandardCharsets.UTF_8);

        ArrayList<Book> books = new ArrayList<>();
        observableBooks = FXCollections.observableList(books);

        bookList.setCellFactory(new BookCellFactory());

        bookList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldBook, newBook) -> {
            if (newBook != null) {
                selectedBook = newBook;
                setBookInformation(newBook);
            } else {
                clearBookInformation();
                selectedBook = null;
            }
        });

        bookList.getItems().addAll(observableBooks);

        removeBookByTitle.textProperty().addListener((observableValue, oldText, newText) -> {
            setRelatedFieldEmpty(removeBookByBarcode, oldText, newText);
        });

        removeBookByBarcode.textProperty().addListener((observableValue, oldText, newText) -> {
            setRelatedFieldEmpty(removeBookByTitle, oldText, newText);
        });

        checkBooksField.textProperty().addListener(((observableValue, oldText, newText) -> {
            boolean textAdded = !newText.trim().isEmpty();

            if (textAdded) {
                clearSelectedBook();
            }
        }));
    }

    /**
     * Clears a related TextField when text is entered into the selected TextField.
     * Also clears the selected book being displayed.
     * @param relatedField TextField sibling of the selected TextField.
     * @param oldText Text before the text was changed.
     * @param newText Text added during text change.
     */
    private void setRelatedFieldEmpty(TextField relatedField, String oldText, String newText) {
        boolean textAdded = !newText.trim().isEmpty();

        if (textAdded) {
            relatedField.setText("");
            clearSelectedBook();
        }
    }

    /**
     * Selects a book and sets its focus if present in the ListView.
     * @param selectedId Int barcode of book to set selected.
     */
    private void setSelectedBook(int selectedId) {
        for (Book book : observableBooks) {
            if (selectedId == book.getBarcode()) {
                int index = bookList.getItems().indexOf(book);
                bookList.getSelectionModel().select(index);
                bookList.getFocusModel().focus(index);
                selectedBook = bookList.getItems().get(index);
                return;
            }
        }
    }

    /**
     * Evaluates a user's request to delete a book. Attempts retrieval of a book object
     * from the internal database. If the book exists, will seek user confirmation on the
     * delete request.
     * @param event On click event triggered by user.
     */
    public void onRemoveBook(ActionEvent event) {
        Book book;

        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = removeBookByTitle.getText().trim();
            String bookBarcode = removeBookByBarcode.getText().trim();

            if (!bookTitle.isEmpty()) {
                book = libraryCore.findBookByTitle(bookTitle);
            } else if (!bookBarcode.isEmpty()) {
                try {
                    int barcode = Integer.parseInt(bookBarcode);
                    book = libraryCore.findBookByBarcode(barcode);
                } catch (NumberFormatException e) {
                    this.invokeError("Barcode must be an numeric value.");
                    return;
                }
            } else {
                return;
            }
        }

        if (book == null) {
            this.invokeError("This book does not match any books in the library's collection.");
            return;
        }

        this.invokeDeleteConfirmation("Are you sure you want to delete " +
                book.getTitle() + "\nfrom the library", book);
    }

    /**
     * Responds to the user's request to check out a book. If the proper information is
     * given, instructs the application layer to process the request.
     * @param event On click event triggered by user.
     */
    public void onCheckOutBooks(ActionEvent event) {
        Book book;

        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                return;
            }

            book = libraryCore.findBookByTitle(bookTitle);
        }

        if (book == null) {
            this.invokeError("This book does not match any books in the library's collection.");
            return;
        }

        libraryCore.checkOutBook(book, this);
    }

    /**
     * Responds to the user's request to check in a book. If the proper information is
     * given, instructs the application layer to process the request.
     * @param event On click event triggered by user.
     */
    public void onCheckInBooks(ActionEvent event) {
        Book book;

        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                return;
            }

            book = libraryCore.findBookByTitle(bookTitle);

            if (book == null) {
                this.invokeError("The title you entered does not match any books in the library's collection.");
                return;
            }
        }

        libraryCore.checkInBook(book, this);
    }

    /**
     * Prompts user to provide a text file using a FileChooser. Instructs the application layer
     * to process the provided file for Book objects.
     * @param event On click event triggered by user.
     */
    public void onAddBooksFromFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            libraryCore.getBooksFromTextFile(selectedFile.getAbsolutePath(), this);
        }
    }

    /**
     * Launches a dialog to obtain database connection information from the user
     * for an external database. Instructs the application layer to attempt connection
     * with the provided database to process Book objects.
     * @param event On click event triggered by user.
     */
    public void onAddBooksFromDatabaseClick(ActionEvent event) {
        DatabaseFormDialog databaseForm = new DatabaseFormDialog();
        databaseForm.showAndWait();

        if (databaseForm.getResult() == ButtonType.OK) {
            try {
                SQLForm form = (SQLForm) databaseForm.getDialogPane().getUserData();
                libraryCore.getBooksFromDatabase(form, this);
            } catch (Exception e) {
                this.invokeError("There was an issue processing your form. Please try again.");
            }
        }
    }

    /**
     * Displays the library's collection to the Book ListView.
     * @param event On click event triggered by user.
     */
    public void onDisplayBooks(ActionEvent event) {
        ArrayList<Book> books = libraryCore.getLibraryBooks();

        observableBooks.clear();
        observableBooks.addAll(books);

        bookList.setItems(observableBooks);
    }

    /**
     * Display's a book's information on the user interface.
     * @param book Book to display.
     */
    private void setBookInformation(Book book) {
        bookTitleText.setText(book.getTitle());
        bookBarcodeText.setText(String.valueOf(book.getBarcode()));
        bookAuthorText.setText(book.getAuthor());
        bookGenreText.setText(book.getGenre());
        bookStatusText.setText(book.getBookStatus().toString());
        bookDueDateText.setText(book.getDueDate());
    }

    /**
     * Removes information on a single Book object from the user interface.
     */
    private void clearBookInformation() {
        bookTitleText.setText("None");
        bookBarcodeText.setText("None");
        bookAuthorText.setText("None");
        bookGenreText.setText("None");
        bookStatusText.setText("None");
        bookDueDateText.setText("None");
    }

    /**
     * Clears the selected Book object and removes its focus.
     */
    private void clearSelectedBook() {
        bookList.getSelectionModel().clearSelection();
        selectedBook = null;
    }

    /**
     * Displays a message to the user.
     * @param message String message to display.
     */
    @Override
    public void invokeMessage(String message) {
        MessageDialog messageDialog = new MessageDialog(message, MessageType.INFORMATIVE);
        messageDialog.show();
    }

    /**
     * Displays an error message to the user.
     * @param message String message to display.
     */
    @Override
    public void invokeError(String message) {
        MessageDialog errorDialog = new MessageDialog(message, MessageType.ERROR);
        errorDialog.show();
    }

    /**
     * Asks the user for confirmation before deleting a book from the database.
     * @param message String confirmation message to display.
     * @param book Book object to be deleted.
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

    /**
     * Updates the ListView with the updated contents of the library
     * database.
     * @param updatedBooks Book ArrayList of books from the library database.
     */
    @Override
    public void updateContent(ArrayList<Book> updatedBooks) {
        int selectedId = -1;
        if (selectedBook != null) {
            selectedId = selectedBook.getBarcode();
        }

        observableBooks.clear();
        observableBooks.addAll(updatedBooks);

        if (selectedId > -1) {
            setSelectedBook(selectedId);
        }
    }

    /**
     * Displays new books provided by the user and asks the user
     * for confirmation before adding them to the library database.
     * @param newBooks Book ArrayList representing new books to add to library.
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