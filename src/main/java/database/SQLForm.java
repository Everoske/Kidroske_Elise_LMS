package database;

/**
 * This class represents a data object used for transporting external database connection
 * information from the user to the application layer where it can be processed.
 * @author Elise Kidroske
 */
public class SQLForm {
    private final SQLSourceType sourceType;
    private final String tableName;

    private String dataSource;
    private String databaseName;
    private String username;
    private String password;
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
