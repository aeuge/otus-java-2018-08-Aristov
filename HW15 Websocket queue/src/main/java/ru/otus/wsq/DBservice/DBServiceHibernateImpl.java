package ru.otus.wsq.DBservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Service;
import ru.otus.wsq.cache.CacheElement;
import ru.otus.wsq.cache.CacheEngine;
import ru.otus.wsq.dao.UsersHibernateDAO;
import ru.otus.wsq.dataset.*;
import ru.otus.wsq.db.DBHibernateConfiguration;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.messagesystem.Addressee;
import ru.otus.wsq.messagesystem.MessageSystem;
import ru.otus.wsq.messagesystem.MessageSystemContext;
import java.util.List;
import java.util.function.Function;

@Service
public class DBServiceHibernateImpl implements DBService, Addressee {
    private Address address;
    private SessionFactory sessionFactory;
    private CacheEngine<Long, UsersDataSet> cache;

    private MessageSystemContext context;

    public DBServiceHibernateImpl(CacheEngine cacheEngine, String address, MessageSystemContext messageSystemContext) {
        this.cache= cacheEngine;
        this.address = new Address(address);
        sessionFactory = createSessionFactory(DBHibernateConfiguration.fill());
        context = messageSystemContext;
        context.setDbAddress(this.address);
        context.getMessageSystem().addAddressee(this);
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

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
