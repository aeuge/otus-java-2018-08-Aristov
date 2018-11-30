package ru.otus.hibernateORM.dao;

import ru.otus.hibernateORM.dataset.DataSet;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import java.util.List;

public interface DAO {
     <T extends DataSet> void save(T dataSet);

     <T extends DataSet> T read(long id, Class<T> clazz);

     <T extends DataSet> T  readByName(String name, Class<T> clazz);

     <T extends DataSet> List<T> readAll(Class<T> clazz);
}
