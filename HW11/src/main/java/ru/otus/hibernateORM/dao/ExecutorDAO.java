package ru.otus.hibernateORM.dao;

import ru.otus.hibernateORM.connection.DBConnection;
import ru.otus.hibernateORM.dataset.AddressDataSet;
import ru.otus.hibernateORM.dataset.DataSet;
import ru.otus.hibernateORM.dataset.PhoneDataSet;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import ru.otus.hibernateORM.db.DBStructureCreator;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutorDAO implements DAO {
    private DBConnection connection;
    private Map<Class, List<Field>> mapOfFieldsForClasses = new HashMap<>();
    private Map<Class, List<Field>> mapOfFieldsForClassesOneToOne = new HashMap<>();
    private Map<Class, List<Field>> mapOfFieldsForClassesOneToMany = new HashMap<>();
    private Map<Class, String> mapOfInserts = new HashMap<>();

    public ExecutorDAO() {
        connection = new DBConnection();
        try {
            DBStructureCreator.generateDefaultTable(connection);
            getAllNonTransientFields(AddressDataSet.class);
            getAllNonTransientFields(PhoneDataSet.class);
            getAllNonTransientFields(UsersDataSet.class);
            System.out.println("classes loaded in ExecutorDAO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection established");
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        if (dataSet.getClass() != null) {
            if (mapOfFieldsForClasses.get(dataSet.getClass()) == null) {
                getAllNonTransientFields(dataSet.getClass());
            }
            //основные поля в БД
            try (PreparedStatement statement = connection.prepareStatement(mapOfInserts.get(dataSet.getClass()))) {
                List<Field> listOfFieldsInClass = mapOfFieldsForClasses.get(dataSet.getClass());
                for (int i = 0; i < listOfFieldsInClass.size(); i++) {
                    statement.setString(i + 1, listOfFieldsInClass.get(i).get(dataSet).toString());
                }
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //OneToOne в БД
            List<Field> listOfFieldsOneToOneInClass = mapOfFieldsForClassesOneToOne.get(dataSet.getClass());
            if (listOfFieldsOneToOneInClass != null) {
                for (int i = 0; i < listOfFieldsOneToOneInClass.size(); i++) {
                    Class clazz = listOfFieldsOneToOneInClass.get(i).getType();
                    String insertStatement = makeInsertStatement(clazz, mapOfFieldsForClasses.get(clazz), ", " + dataSet.getClass().getSimpleName() + "_id", ", ?");
                    Object valueOfOnetoOneField = null;
                    try {
                        valueOfOnetoOneField = listOfFieldsOneToOneInClass.get(i).get(dataSet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    try (PreparedStatement statement = connection.prepareStatement(insertStatement)) {
                        List<Field> listOfFieldsInClass = mapOfFieldsForClasses.get(clazz);
                        for (int j = 0; j < listOfFieldsInClass.size(); j++) {
                            statement.setString(j + 1, listOfFieldsInClass.get(j).get(valueOfOnetoOneField).toString());
                        }
                        statement.setLong(listOfFieldsInClass.size() + 1, dataSet.getId());
                        int numberOfInsertedRecords = statement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //OneToMany в БД
            List<Field> listOfFieldsOneToManyInClass = mapOfFieldsForClassesOneToMany.get(dataSet.getClass());
            if (listOfFieldsOneToManyInClass != null) {
                for (int i = 0; i < listOfFieldsOneToManyInClass.size(); i++) {
                    Field field = listOfFieldsOneToManyInClass.get(i);
                    Class<?> clazz = getClassForList(field);
                    String insertStatement = makeInsertStatement(clazz, mapOfFieldsForClasses.get(clazz), ", " + dataSet.getClass().getSimpleName() + "_id", ", ?");
                    List<Object> valueOfOnetoManyField = null;
                    try {
                        valueOfOnetoManyField = (List<Object>) listOfFieldsOneToManyInClass.get(i).get(dataSet);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    List<Field> listOfFieldsInClass = mapOfFieldsForClasses.get(clazz);
                    try (PreparedStatement statement = connection.prepareStatement(insertStatement)) {
                        for (int k = 0; k < valueOfOnetoManyField.size(); k++) {
                            for (int j = 0; j < listOfFieldsInClass.size(); j++) {
                                statement.setString(j + 1, listOfFieldsInClass.get(j).get(valueOfOnetoManyField.get(k)).toString());
                            }
                            statement.setLong(listOfFieldsInClass.size() + 1, dataSet.getId());
                            int numberOfInsertedRecords = statement.executeUpdate();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("class not found");
        }
    }

    private Class getClassForList(Field field) {
        Type type = (ParameterizedType) field.getGenericType();
        String typeName = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
        Class<?> clazz = null;
        try  {
            clazz = Class.forName(typeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    @Override
    public <T extends DataSet> T read(long id, Class<T> clazz) {
        if (clazz != null) {
            if (mapOfFieldsForClasses.get(clazz) == null) {
                getAllNonTransientFields(clazz);
            }
            T dataset = null;
            try (PreparedStatement statement = connection.prepareStatement("select * from " + clazz.getSimpleName() + " where id=?")) {
                dataset = clazz.getConstructor().newInstance();
                statement.setLong(1, id);
                statement.executeQuery();
                ResultSet rs = statement.getResultSet();
                if (rs.next()) {
                    for (Field f: mapOfFieldsForClasses.get(clazz)) {
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
            //OneToOne
            List<Field> listOfFieldsOneToOneInClass = mapOfFieldsForClassesOneToOne.get(clazz);
            for (int i = 0; i < listOfFieldsOneToOneInClass.size(); i++) {
                Field field = listOfFieldsOneToOneInClass.get(i);
                Class clazzOneToOne = field.getType();
                T datasetOneToOne = null;
                try (PreparedStatement statement = connection.prepareStatement("select * from " + clazzOneToOne.getSimpleName() + " where " + clazz.getSimpleName() + "_id=?")) {
                    datasetOneToOne = (T) clazzOneToOne.getConstructor().newInstance();
                    statement.setLong(1, id);
                    statement.executeQuery();
                    ResultSet rs = statement.getResultSet();
                    if (rs.next()) {
                        for (Field f : mapOfFieldsForClasses.get(clazzOneToOne)) {
                            f.setAccessible(true);
                            Object fieldValue = rs.getObject(f.getName());
                            if (fieldValue != null) {
                                f.set(datasetOneToOne, fieldValue);
                            }
                        }
                        field.set(dataset, datasetOneToOne);
                    } else {
                        throw new SQLException();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //OneToMany
            List<Field> listOfFieldsOneToManyInClass = mapOfFieldsForClassesOneToMany.get(clazz);
            for (int i = 0; i < listOfFieldsOneToManyInClass.size(); i++) {
                Field field = listOfFieldsOneToManyInClass.get(i);
                Class<?> clazzOneToMany = getClassForList(field);
                List<T> datasetOneToMany = new ArrayList<>();
                try (PreparedStatement statement = connection.prepareStatement("select * from " + clazzOneToMany.getSimpleName() + " where " + clazz.getSimpleName() + "_id=?")) {
                    statement.setLong(1, id);
                    statement.executeQuery();
                    ResultSet rs = statement.getResultSet();
                    while (rs.next()) {
                        T datasetSimple = null;
                        datasetSimple = (T) clazzOneToMany.getConstructor().newInstance();
                        for (Field f : mapOfFieldsForClasses.get(clazzOneToMany)) {
                            f.setAccessible(true);
                            Object fieldValue = rs.getObject(f.getName());
                            if (fieldValue != null) {
                                f.set(datasetSimple, fieldValue);
                            }
                        }
                        datasetOneToMany.add(datasetSimple);
                        field.set(dataset, datasetOneToMany);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return dataset;
        } else {
            return null;
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

    private void getAllNonTransientFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field f: declaredFields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                boolean isPrimitive = f.getType().isPrimitive();
                boolean isBasic = isBasic(f.getType());
                boolean isOneToOne = f.isAnnotationPresent(OneToOne.class);
                boolean isOneToMany = f.isAnnotationPresent(OneToMany.class);
                if (!isTransient) {
                    f.setAccessible(true);
                    if  (isPrimitive || isBasic) {
                        fields.add(f);
                    } else if (isOneToOne) {
                        //fields.add(f);
                        List<Field> lf = mapOfFieldsForClassesOneToOne.get(clazz);
                        if (lf == null) {
                            lf = new ArrayList<>();
                        }
                        lf.add(f);
                        mapOfFieldsForClassesOneToOne.put(clazz,lf);
                    } else if (isOneToMany) {
                        List<Field> lf = mapOfFieldsForClassesOneToMany.get(clazz);
                        if (lf == null) {
                            lf = new ArrayList<>();
                        }
                        lf.add(f);
                        mapOfFieldsForClassesOneToMany.put(clazz,lf);
                    }
                }
            }
        }
        mapOfFieldsForClasses.put(clazz, fields);
        String insertStatement = makeInsertStatement(clazz, fields, "", "");
        mapOfInserts.put(clazz,insertStatement);
    }

    private String makeInsertStatement(Class clazz, List<Field> lf, String additionalField, String additionalValue) {
        List<String> fieldList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        lf.forEach((field)->{
            fieldList.add(field.getName());
            valueList.add("?");
        });
        String insertStatement = "insert into " + clazz.getSimpleName() + " (" + String.join(",", fieldList) +  additionalField + ")" + " values (" + String.join(",", valueList) + additionalValue + ")";
        return insertStatement;
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
