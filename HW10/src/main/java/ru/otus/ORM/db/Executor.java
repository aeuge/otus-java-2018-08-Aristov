package ru.otus.ORM.db;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import ru.otus.ORM.connection.DBConnection;
import ru.otus.ORM.dataset.DataSet;

public class Executor implements AutoCloseable {
    private DBConnection connection;
    private List<Field> fields = new ArrayList<>();
    private Class clazz = null;
    private String InsertStatement = null;
    private String SelectStatement = "select * from orm where id=?";

    public Executor() {
        connection = new DBConnection();
        System.out.println("Connection established");
    }

    public <T extends DataSet> void save(T user) throws SQLException {
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
                throw new SQLException();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, IllegalAccessException {
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
            throw new IllegalAccessException();
        }
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
}
