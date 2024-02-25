package Controller;

import Domain.Book;

import java.util.ArrayList;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Description:
Used to allow the application layer to respond to the controller layer without directly
accessing the controller layer
 */
public interface IInformable {
    void invokeError(String message);
    void invokeConfirmation(String message);
    void updateContent(ArrayList<Book> updatedBooks);
    void invokePreview(ArrayList<Book> newBooks);

}
