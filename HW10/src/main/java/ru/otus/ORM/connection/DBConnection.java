package ru.otus.ORM.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection implements AutoCloseable {
    private final Connection connection;

    public DBConnection() {
        connection = ConnectionHelper.getConnection();
    }

    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void close() throws Exception {
        connection.close();
        System.out.println("Connection closed. Bye!");
    }

    protected Connection getConnection() {
        return connection;
    }

    public PreparedStatement prepareStatement (String str) throws SQLException {
        return connection.prepareStatement(str);
    }

    public Statement createStatement () throws SQLException {
        return connection.createStatement();
    }
}
