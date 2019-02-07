package ru.otus.socket.sdbs.DBservice;

import org.apache.log4j.lf5.util.AdapterLogRecord;
import ru.otus.socket.sms.dataset.DataSet;
import ru.otus.socket.sms.messagesystem.Address;

import java.sql.SQLException;
import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();

    <T extends DataSet> void save(T dataset);

    <T extends DataSet> T read(long id, Class<T> clazz) throws SQLException;

    <T extends DataSet> T readByName(String name, Class<T> clazz);

    <T extends DataSet> List<T> readAll(Class<T> clazz);

    int getCacheCount();

    Address getAddress();

}
