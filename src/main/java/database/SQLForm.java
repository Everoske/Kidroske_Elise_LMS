package database;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 04/07/2024
Name: SQL Form
Description:
A data object used for transporting external database connection information from
the user through the application. Used for accessing an external database
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
