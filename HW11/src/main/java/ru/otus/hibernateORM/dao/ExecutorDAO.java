package ru.otus.hibernateORM.dao;

import ru.otus.hibernateORM.connection.DBConnection;
import ru.otus.hibernateORM.dataset.DataSet;
import ru.otus.hibernateORM.db.DBStructureCreator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecutorDAO implements DAO {
    private DBConnection connection;
    private List<Field> fields = new ArrayList<>();
    private Class clazz = null;
    private String InsertStatement = null;
    private String SelectStatement = "select * from orm where id=?";

    public ExecutorDAO() {
        connection = new DBConnection();
        try {
            DBStructureCreator.generateDefaultTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection established");
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        if ((clazz != dataSet.getClass()) && (dataSet.getClass() != null)){
            fields = getAllNonTransientFields(dataSet.getClass());
        }
        try (PreparedStatement statement = connection.prepareStatement(InsertStatement)){
            for (int i = 0; i < fields.size(); i++) {
                statement.setString(i + 1, fields.get(i).get(dataSet).toString());
            }
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public <T extends DataSet> T read(long id, Class<T> clazz) {
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
    }

    @Override
    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        return null;
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
                boolean isPrimitive = f.getType().isPrimitive();
                boolean isBasic = isBasic(f.getType());
                if ((!isTransient) && (isPrimitive || isBasic)) {
                    f.setAccessible(true);
                    fields.add(f);
                    fieldList.add(f.getName());
                    valueList.add("?");
                }
            }
        }
        InsertStatement = "insert into orm (" + String.join(",", fieldList) + ")" + " values (" + String.join(",", valueList) + ")";
        return fields;
    }

    private boolean isBasic (Class clazz) {
        return (clazz == String.class) || (clazz == Integer.class) || (clazz == Character.class) || (clazz == Float.class) || (clazz == Short.class) || (clazz == Double.class) || (clazz == Long.class) || (clazz == Byte.class) || (clazz == Boolean.class);
    }

    public DBConnection getConnection() {
        return connection;
    }

    public void close () throws Exception {
        connection.close();
    }

}
