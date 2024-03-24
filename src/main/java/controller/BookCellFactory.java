package controller;

import domain.Book;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class BookCellFactory implements Callback<ListView<Book>, ListCell<Book>> {
    @Override
    public ListCell<Book> call(ListView<Book> bookListView) {
        return new ListCell<>() {
            @Override
            public void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getTitle() + " | " + book.getBarcode() + " | " + book.getAuthor());
                }
            }
        };
    }
}
