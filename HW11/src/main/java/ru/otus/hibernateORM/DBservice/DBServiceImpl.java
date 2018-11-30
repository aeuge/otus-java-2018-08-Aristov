package ru.otus.hibernateORM.DBservice;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import ru.otus.hibernateORM.connection.DBConnection;
import ru.otus.hibernateORM.dataset.DataSet;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import ru.otus.hibernateORM.db.DBStructureCreator;

public class DBServiceImpl implements DBService, AutoCloseable {
    private DBConnection connection;
    private List<Field> fields = new ArrayList<>();
    private Class clazz = null;
    private String InsertStatement = null;
    private String SelectStatement = "select * from orm where id=?";

    public DBServiceImpl() {
        connection = new DBConnection();
        try {
            DBStructureCreator.generateDefaultTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection established");
    }

    public <T extends DataSet> void save(T user) {
        if (user != null) {
            if ((clazz != user.getClass()) && (user.getClass() != null)){
                fields = getAllNonTransientFields(user.getClass());
            }
            try (PreparedStatement statement = connection.prepareStatement(InsertStatement)){
                for (int i = 0; i < fields.size(); i++) {
                    statement.setString(i + 1, fields.get(i).get(user).toString());
                }
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    public <T extends DataSet> T read(long id, Class<T> clazz) throws SQLException {
        if (clazz != null) {
            if (this.clazz != clazz) {
                fields = getAllNonTransientFields(clazz);
            }
            T dataset = null;
            try (PreparedStatement statement = connection.prepareStatement(SelectStatement)) {
                dataset = clazz.getConstructor().newInstance();
                statement.setLong(1, id);
                statement.executeQuery();
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    for (Field f: fields) {
                        f.setAccessible(true);
                        Object fieldValue = rs.getObject(f.getName());
                        if (fieldValue != null) {
                            f.set(dataset, fieldValue);
                        }
                    }
                } else {
                    throw new SQLException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dataset;
        } else {
            throw new SQLException();
        }
    }

    @Override
    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        return null;
    }

    public DBConnection getConnection() {
        return connection;
    }

    private List<Field> getAllNonTransientFields(Class<?> type) {
        this.clazz = type;
        List<Field> fields = new ArrayList<>();
        List<String> fieldList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field f: declaredFields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                if (!isTransient) {
                    f.setAccessible(true);
                    fields.add(f);
                    fieldList.add(f.getName());
                    valueList.add("?");
                }
            }
        }
        this.InsertStatement =  "insert into orm (" + String.join(",", fieldList ) + ")" + " values (" + String.join(",", valueList)  + ")";
        return fields;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public String getLocalStatus() {
        return connection == null ? "ACTIVE" : "INACTIVE";
    }



}
