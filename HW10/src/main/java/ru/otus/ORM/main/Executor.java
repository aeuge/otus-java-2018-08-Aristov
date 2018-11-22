package ru.otus.ORM.main;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import ru.otus.ORM.connection.DBConnection;
import ru.otus.ORM.helper.DataSet;

public class Executor {
    private DBConnection connection;

    public Executor() {
        connection = new DBConnection();
    }

    public <T extends DataSet> void save(T user) throws SQLException {
        if (user!=null) {
            try {
                List<Field> fields = getAllFields(user.getClass());
                String insertStatement = getInsertStatement(fields, user);
                System.out.println(insertStatement);
                Statement statement = connection.createStatement();
                statement.execute(insertStatement);
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, IllegalAccessException {
        T dataset = null;
        try {
            dataset = clazz.getConstructor().newInstance();
            String selectStatement = "select * from orm where id=" + id;
            Statement statement = connection.createStatement();
            statement.execute(selectStatement);
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                List<Field> fields = getAllFields(clazz);
                for (Field f : fields) {
                    boolean isTransient = Modifier.isTransient(f.getModifiers());
                    if (!isTransient) {
                        f.setAccessible(true);
                        Object fieldValue = rs.getObject(f.getName());
                        if (fieldValue != null) {
                            f.set(dataset, fieldValue);
                        }
                    }
                }
            } else {
                throw new SQLException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }


    public DBConnection getConnection() {
        return connection;
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    private <T extends DataSet> String getInsertStatement(List<Field> fields, T user) throws IllegalAccessException {
        List<String> fieldList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        for (Field f : fields) {
            boolean isTransient = Modifier.isTransient(f.getModifiers());
            if (!isTransient) {
                f.setAccessible(true);
                if (f.get(user) != null) {
                    fieldList.add(f.getName());
                    valueList.add("\'"+f.get(user).toString()+"\'");
                }
            }
        }
        return  "insert into orm ("+String.join(",", fieldList) + ")" + " values ("+String.join(",", valueList)  +")";
    }
}
