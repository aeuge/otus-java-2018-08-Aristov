package ru.otus.war.DBservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.war.cache.CacheElement;
import ru.otus.war.cache.CacheEngine;
import ru.otus.war.dao.UsersHibernateDAO;
import ru.otus.war.dataset.*;
import ru.otus.war.db.DBHibernateConfiguration;
import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;
    CacheEngine<Long, UsersDataSet> cache;

    public DBServiceHibernateImpl(CacheEngine cacheEngine) {
        cache = cacheEngine;
        sessionFactory = createSessionFactory(DBHibernateConfiguration.fill());
    }

    public DBServiceHibernateImpl(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public String getLocalStatus() {
        return runInSession(session -> {
            return session.getTransaction().getStatus().name();
        });
    }

    public <T extends DataSet> void save(T dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UsersHibernateDAO dao = new UsersHibernateDAO(session);
            dao.save(dataSet);
        }
    }

    public <T extends DataSet> T read(long id, Class<T> clazz) {
        CacheElement<Long, UsersDataSet> element = cache.get(id);
        if (element != null) return (T) element.getValue();
        return runInSession(session -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(session);
            return dao.read(id, clazz);
        });
    }

    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        return runInSession(session -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(session);
            return dao.readByName(name, clazz);
        });
    }

    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        return runInSession(session -> {
            UsersHibernateDAO dao = new UsersHibernateDAO(session);
            List<T> list = dao.readAll(clazz);
            for (T el : list) {
                cache.put(new CacheElement<>(el.getId(),(UsersDataSet) el));
            }

            return list;
        });
    }

    @Override
    public int getCacheCount() { return cache.getCount(); }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    @Override
    public void close() throws Exception {
        sessionFactory.close();
    }
}
