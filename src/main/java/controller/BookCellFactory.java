package controller;

import domain.Book;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * This class determines how a Book object is presented in a ListView.
 * @author Elise Kidroske
 */
public class BookCellFactory implements Callback<ListView<Book>, ListCell<Book>> {
    /**
     * Determines how a Book object should be displayed in a ListView.
     * @param bookListView Book ListView used in the user interface.
     * @return ListCell containing the formatting for Book objects.
     */
    @Override
    public ListCell<Book> call(ListView<Book> bookListView) {
        return new ListCell<>() {
            @Override
            public void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getBarcode() + " | " + book.getTitle() + " | " + book.getAuthor());
                }
            }
        };
    }
}
