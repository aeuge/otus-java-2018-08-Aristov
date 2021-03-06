package ru.otus.war;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.war.DBservice.DBService;
import ru.otus.war.DBservice.DBServiceHibernateImpl;
import ru.otus.war.cache.CacheEngineImpl;
import ru.otus.war.dataset.AddressDataSet;
import ru.otus.war.dataset.PhoneDataSet;
import ru.otus.war.dataset.UsersDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);
    private DBService dbService;

    @Test
    public void commonHibernate() {
        tryLoadAndSaveUserDataSet();
    }

    private void tryLoadAndSaveUserDataSet() {
        try {
            UsersDataSet user1 = new UsersDataSet(
                    "tully",
                    new AddressDataSet("Mira"),
                    new PhoneDataSet("+1 234 567 8018"),
                    new PhoneDataSet("+7 987 645 4545")
            );
            dbService.save(user1);
            System.out.println("ОБъект отправлен в БД: " + user1);
            UsersDataSet user1Loaded = dbService.read(1, UsersDataSet.class);
            System.out.println("ОБъект извлечен из БД: " + user1Loaded);
            Assertions.assertEquals(user1, user1Loaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readByNameHibernate() {
            tryLoadAndSaveUserDataSet2();
    }

    private void tryLoadAndSaveUserDataSet2() {
        try {
            UsersDataSet user2 = new UsersDataSet(
                    "sully",
                    new AddressDataSet("Truda"),
                    new PhoneDataSet("+67 890 344 4422")
            );
            dbService.save(user2);
            UsersDataSet user2Loaded = dbService.readByName("sully", UsersDataSet.class);
            System.out.println(user2Loaded);
            Assertions.assertEquals(user2, user2Loaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readAllHibernate() {
            tryLoadAndSaveUserDataSet();
            tryLoadAndSaveUserDataSet2();
            List<UsersDataSet> dataSets = dbService.readAll(UsersDataSet.class);
            for (UsersDataSet userDataSet: dataSets)
                System.out.println("Read all: " + userDataSet);
    }

}
