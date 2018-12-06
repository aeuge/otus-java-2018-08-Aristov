package ru.otus.hibernateORM.db;

import ru.otus.hibernateORM.connection.DBConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class DBStructureCreator {
    private static final String CREATE_TABLE = "create table UsersDataSet (id bigint(20) NOT NULL auto_increment,\n" +
            "name varchar(255),\n" +
            "age int(3));";
    private static final String CREATE_TABLE_ADDRESS = "create table AddressDataSet (id bigint(20) NOT NULL auto_increment,\n" +
            "street varchar(255),\n" +
            "UsersDataSet_id bigint(20));";
    private static final String CREATE_TABLE_PHONES = "create table PhoneDataSet (id bigint(20) NOT NULL auto_increment,\n" +
            "number varchar(255),\n" +
            "UsersDataSet_id bigint(20));";

    public static void generateDefaultTable(DBConnection connection) throws SQLException {
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(CREATE_TABLE);
                statement.executeUpdate(CREATE_TABLE_ADDRESS);
                statement.executeUpdate(CREATE_TABLE_PHONES);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
