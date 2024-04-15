package controller;

import domain.Book;
import java.util.ArrayList;

/**
 * This interface is used to allow for the application layer to call methods within the controller
 * layer without directly accessing the controller layer.
 * @author Elise Kidroske
 */
public interface IBookController {
    /**
     * Used to display a message.
     * @param message String message to display.
     */
    void invokeMessage(String message);

    /**
     * Used to display an error message.
     * @param message String error message to display.
     */
    void invokeError(String message);

    /**
     * Used to get confirmation from the user before deleting the provided book.
     * @param message String confirmation message.
     * @param book Book object to be deleted.
     */
    void invokeDeleteConfirmation(String message, Book book);

    /**
     * Used to update the controller's observable list.
     * @param updatedBooks Book ArrayList containing books from the library database.
     */
    void updateContent(ArrayList<Book> updatedBooks);

    /*
    Name: Invoke Preview
    Arguments: ArrayList<Book> representing new books from text file
    Returns: Void
    Description:
    Used to show the user a preview of the books they would like to add
     */

    /**
     * Used to display new books provided by the user before adding them to the
     * library database.
     * @param newBooks Book ArrayList representing new books to add to library.
     */
    void invokePreview(ArrayList<Book> newBooks);

}
