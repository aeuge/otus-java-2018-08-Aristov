package ru.otus.wsq.DBservice;

import ru.otus.wsq.dataset.DataSet;
import ru.otus.wsq.messagesystem.MessageSystem;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();

    <T extends DataSet> void save(T dataset);

    <T extends DataSet> T read(long id, Class<T> clazz) throws SQLException;

    <T extends DataSet> T readByName(String name, Class<T> clazz);

    <T extends DataSet> List<T> readAll(Class<T> clazz);

    int getCacheCount();

    MessageSystem getMS();
}
