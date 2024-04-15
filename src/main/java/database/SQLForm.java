package database;

/**
 * This class represents a data object used for transporting external database connection
 * information from the user to the application layer where it can be processed.
 * @author Elise Kidroske
 */
public class SQLForm {
    /** Represents the type of SQL database.*/
    private final SQLSourceType sourceType;
    /** The table to access within the database.*/
    private final String tableName;
    /** The server the database is hosted on.*/
    private String dataSource;
    /** The name of the database to access.*/
    private String databaseName;
    /** The username used to access the database.*/
    private String username;
    /** The password corresponding with the username.*/
    private String password;
    /** An absolute path to a database file to open.*/
    private String databaseFilePath;

    public SQLForm(SQLSourceType sourceType, String tableName,
                   String dataSource, String databaseName,
                   String username, String password) {
        this.sourceType = sourceType;
        this.tableName = tableName;
        this.dataSource = dataSource;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public SQLForm(SQLSourceType sourceType, String tableName, String databaseFilePath) {
        this.sourceType = sourceType;
        this.tableName = tableName;
        this.databaseFilePath = databaseFilePath;
    }

    public SQLSourceType getSourceType() {
        return sourceType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseFilePath() {
        return databaseFilePath;
    }
}
