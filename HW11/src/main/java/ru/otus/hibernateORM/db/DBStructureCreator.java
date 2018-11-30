package ru.otus.hibernateORM.db;

import ru.otus.hibernateORM.connection.DBConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class DBStructureCreator {
    private static final String CREATE_TABLE = "create table orm (id bigint(20) NOT NULL auto_increment,\n" +
            "name varchar(255),\n" +
            "age int(3));";

    public static void generateDefaultTable(DBConnection connection) throws SQLException {
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
