package ru.otus.socket.sdbs.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.socket.sms.dataset.DataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UsersHibernateDAO {
    private Session session;

    public UsersHibernateDAO(Session session) {
        this.session = session;
    }

    public <T extends DataSet> void save(T dataSet) {
        session.save(dataSet);
    }

    public <T extends DataSet> T read(long id, Class<T> clazz) {
            return session.get(clazz, id);
    }

    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> from = criteria.from(clazz);
        criteria.where(builder.equal(from.get("name"), name));
        Query<T> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).list();
    }
}
