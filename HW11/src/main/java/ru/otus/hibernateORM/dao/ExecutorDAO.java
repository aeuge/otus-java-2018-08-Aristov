package ru.otus.hibernateORM.dao;

import ru.otus.hibernateORM.dataset.DataSet;

import java.util.List;

public class ExecutorDAO implements DAO {
    @Override
    public <T extends DataSet> void save(T dataSet) {

    }

    @Override
    public <T extends DataSet> T read(long id, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        return null;
    }
}
