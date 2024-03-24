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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

        // Set Text Change listeners
        removeBookByTitle.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // If the new value is not empty, but the old value is empty, clear contents of
            // remove book by barcode
            if (oldValue.trim().isEmpty() || !newValue.trim().isEmpty()) {
                removeBookByBarcode.setText("");
                clearSelection();
            }
        });

        removeBookByBarcode.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // If the new value is not empty, but the old value is empty, clear contents of
            // remove book by title
            if (oldValue.trim().isEmpty() || !newValue.trim().isEmpty()) {
                removeBookByTitle.setText("");
                clearSelection();
            }
        });

        checkBooksField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearSelection();
            }
        }));
    }

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
                book = libraryCore.findBookByBarcode(bookBarcode);
            } else {
                // Highlight both fields red and return
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

    public void onCheckOutBooks(ActionEvent event) {
        Book book;

        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                // Text text area red
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

    public void onCheckInBooks(ActionEvent event) {
        Book book;

        if (selectedBook != null) {
            book = selectedBook;
        } else {
            String bookTitle = checkBooksField.getText().trim();
            if (bookTitle.isEmpty()) {
                // Text text area red
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

    public void onAddBooksFromFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            libraryCore.getBooksFromTextFile(selectedFile.getAbsolutePath(), this);
        }
    }

    public void onAddBooksFromDatabaseClick(ActionEvent event) {
    }

    public void onDisplayBooks(ActionEvent event) {
        ArrayList<Book> books = libraryCore.getLibraryBooks();
        observableBooks.clear();
        observableBooks.addAll(books);

        bookList.setItems(observableBooks);
    }

    private void setBookInformation(Book book) {
        bookTitleText.setText(book.getTitle());
        bookBarcodeText.setText(book.getBarcode());
        bookAuthorText.setText(book.getAuthor());
        bookGenreText.setText(book.getGenre());
        bookStatusText.setText(book.getBookStatus().toString());
        bookDueDateText.setText(book.getDueDate());
    }

    private void clearBookInformation() {
        bookTitleText.setText("None");
        bookBarcodeText.setText("None");
        bookAuthorText.setText("None");
        bookGenreText.setText("None");
        bookStatusText.setText("None");
        bookDueDateText.setText("None");
    }

    private void clearSelection() {
        bookList.getSelectionModel().clearSelection();
        selectedBook = null;
    }

    @Override
    public void invokeMessage(String message) {
        MessageDialog messageDialog = new MessageDialog(message, MessageType.INFORMATIVE);
        messageDialog.show();
    }

    @Override
    public void invokeError(String message) {
        MessageDialog errorDialog = new MessageDialog(message, MessageType.ERROR);
        errorDialog.show();
    }

    @Override
    public void invokeDeleteConfirmation(String message, Book book) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(message);
        confirmationDialog.showAndWait();

        ButtonType result = confirmationDialog.getResult();

        if (result.equals(ButtonType.OK)) {
            libraryCore.removeBookFromCollection(book, this);
        }
    }

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

    @Override
    public void invokePreview(ArrayList<Book> newBooks) {
        PreviewBooksDialog previewBooks = new PreviewBooksDialog(newBooks);
        previewBooks.showAndWait();

        ButtonType result = previewBooks.getResult();
        if (result.equals(ButtonType.OK)) {
            libraryCore.addBooksToDatabase(newBooks, this);
        }
    }
}