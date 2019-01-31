package ru.otus.wsq.DBservice.db;

import org.hibernate.cfg.Configuration;
import ru.otus.wsq.dataset.*;

public class DBHibernateConfiguration {
    public static Configuration fill() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        return configuration;
    }
}
