package ru.otus.hibernateORM.DBservice;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import ru.otus.hibernateORM.connection.DBConnection;
import ru.otus.hibernateORM.dao.ExecutorDAO;
import ru.otus.hibernateORM.dataset.DataSet;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import ru.otus.hibernateORM.db.DBStructureCreator;

public class DBServiceImpl implements DBService, AutoCloseable {
    private ExecutorDAO executor;

    public DBServiceImpl() {
        executor = new ExecutorDAO();
    }

    @Override
    public String getLocalStatus() {
        return null;
    }

    public <T extends DataSet> void save(T user) {
        if (user != null) {
            executor.save(user);
        } else {
            throw new NoSuchElementException();
        }
    }

    public <T extends DataSet> T read(long id, Class<T> clazz) throws SQLException {
        if (clazz != null) {
            return executor.read(id, clazz);
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

    @Override
    public void close() throws Exception {
        executor.close();
    }
}
