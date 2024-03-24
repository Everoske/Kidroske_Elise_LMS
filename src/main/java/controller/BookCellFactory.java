package controller;

import domain.Book;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/24/2024
Name: BookCellFactory
Description:
This is used as a wrapper for a Book object in a ListView.
It determines how the Book is presented in the ListView.
 */
public class BookCellFactory implements Callback<ListView<Book>, ListCell<Book>> {
    /*
    Name: Call
    Arguments: ListView<Book> parent list view
    Returns: ListCell<Book>
    Description:
    Called when ListView updates, allows us to set cell properties
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
                    setText(book.getTitle() + " | " + book.getBarcode() + " | " + book.getAuthor());
                }
            }
        };
    }
}
