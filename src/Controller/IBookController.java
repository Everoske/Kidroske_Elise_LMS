package Controller;

import Domain.Book;

import java.util.ArrayList;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/01/2024
Name: IBookController
Description:
Used to allow the application layer to respond to the controller layer without directly
accessing the controller layer
 */
public interface IBookController {

    /*
    Name: Invoke Message
    Arguments: String message
    Returns: Void
    Description:
    Used to inform the class that implements this method
     */
    void invokeMessage(String message);

    /*
    Name: Invoke Error
    Arguments: String message
    Returns: Void
    Description:
    Used to inform the class that implements this method that an error has occurred
     */
    void invokeError(String message);

    /*
    Name: Invoke Delete Confirmation
    Arguments: String message, Book to delete
    Returns: Void
    Description:
    Used to get confirmation from the user before deleting the given book
     */
    void invokeDeleteConfirmation(String message, Book book);

    /*
    Name: Update Content
    Arguments: ArrayList<Book> representing library collection
    Returns: Void
    Description:
    Meant to update the controller's observable list
     */
    void updateContent(ArrayList<Book> updatedBooks);

    /*
    Name: Invoke Preview
    Arguments: ArrayList<Book> representing new books from text file
    Returns: Void
    Description:
    Used to show the user a preview of the books they would like to add
     */
    void invokePreview(ArrayList<Book> newBooks);

}
