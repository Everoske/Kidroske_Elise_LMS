## Library Management System

### Overview
This project was designed and built by Elise Kidroske as the final project for Software Development 1 CEN-3024C at Valencia College in Spring 2024. This project is a library management system that utilizes a SQLite database to store and manage books in a library.
It also allows users to import books from text files and external SQLite and MySQL databases. It utilizes JavaFX and xml to construct the UI. CSS is used for styling UI components. Maven is used to build the application into an executable JAR file. 

### Scope of Project
- Users can view all books stored in the library management system.
- Users can remove books by title.
- Users can remove books by barcode.
- Users can check-out books by title.
- Users can check-in books by title.
- Users can view title, barcode, author, genre, books checked status, and due date by clicking on book objects.
- Users can add books from text files.
- Users can add books from an SQLite database by filling out the appropriate form.
- Users can add books from a MySQL database by filling out the appropriate form.

### Project Structure
This project uses an N-tier architecture where the application is broken down into four layers: 
- **Database Layer:** For performing CRUD operations on the internal SQLite database.
- **Application Layer:** For enforcing business logic between the controller layer and interacting with the database layer.
- **Controller Layer:** For responding to user interactions with the UI.
- **Presentation Layer:** For building the user interface and displaying information to the user.

These layers are represented in their respective folders found in src/main/java/. Please note that much of the presentation layer lies in the FXML and CSS files located in /src/main/resources/view/ and /src/main/resources/styles/ respectively.
Domain represents the Book data model. Book objects have a title, barcode, author, genre, books checked status, and due date.

### Breakdown of Project Contents
- **Application Core:** Found in /src/main/java/application/. Represents the application layer of the application.
- **Library Controller:** Found in /src/main/java/controller/. Represents the controller layer of the application.
- **Library Application:** Found in /src/main/java/controller/. This is the FXML application instance.
- **Launcher:** Found in /src/main/java/controller/. This is the launcher for the application.
- **Other Controller Files:** Found in /src/main/java/controller/. Interfaces and components used for displaying book objects and responding to user interactions.
- **Database Manager:** Found in /src/main/java/database/. Represents the database layer of the application.
- **Other Files in Database Folder:** Found in /src/main/java/database/. Files for handling external SQL operations.
- **Domain Files:** Found in /src/main/java/domain/. Files representing the Book object model.
- **Presentation Dialog Files:** Found in /src/main/java/presentation/. Files are for dialogs the user interacts with in the application.
- **Presentation FXML File:** Found in /src/main/resources/view/. Represents the main application UI.
- **Styles:** Found in /src/main/resources/styles/. Includes the CSS styling sheets for the application.
- **Images:** Found in /src/main/resources/images/. Includes the icon images used in the application.
- **Pom File:** Found in the root directory. Includes Maven build configuration and dependencies.
- **SQLite Database:** Found in the root directory. Includes the SQLite database used in the application.
- **Downloadable JAR File:** Found at target/Kidroske_Elise_LMS-1.0-SNAPSHOT-shaded.jar. Includes an executable version of the project.

### How to Install Project
- Locate the shaded JAR file at target/Kidroske_Elise_LMS-1.0-SNAPSHOT-shaded.jar.
- Ensure you have JDK 17 installed or higher.
- Run application.

### Formatting External Text Files and SQL Databases
#### Text Files
- Books are formatted using commas with no spaces between values.
- One line represents one book object.
- Follow this format: TITLE,AUTHOR,GENRE,CHECKED_IN/CHECKED_OUT,
- Due Date is the last item and can be left empty or filled using this format: YYYY-MM-DD

#### SQL 
- Create an SQL database for managing books.
- Create a Table for book objects. Be sure to include TITLE, AUTHOR, GENRE, BOOK_STATUS, and DUE_DATE columns.
- BOOK_STATUS should include a constraint that only allows the values CHECKED_OUT and CHECKED_IN.
- See example SQL below.

### Example Book Database SQL:
CREATE TABLE BOOKS (
     id INTEGER PRIMARY KEY,
     title VARCHAR(100),
     author VARCHAR(50),
     genre VARCHAR(50),
     book_status VARCHAR(20),
     due_date DATE,
     CONSTRAINT CHK_Book_Status CHECK (
          book_status = 'CHECKED_OUT' OR
          book_status = 'CHECKED_IN'
    )
);
