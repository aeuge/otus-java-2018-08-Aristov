package ru.otus.ORM.helper;

import ru.otus.ORM.connection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableORM {
    private static final String CREATE_TABLE = "create table orm (id bigint(20) NOT NULL auto_increment,\n" +
            "name varchar(255),\n" +
            "age int(3));";

    public static void generateDefaultTable(DBConnection connection) throws SQLException {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(CREATE_TABLE);
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
