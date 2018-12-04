package ru.otus.hibernateORM;

import org.h2.tools.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernateORM.DBservice.DBService;
import ru.otus.hibernateORM.DBservice.DBServiceHibernateImpl;
import ru.otus.hibernateORM.dataset.AddressDataSet;
import ru.otus.hibernateORM.dataset.PhoneDataSet;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import ru.otus.hibernateORM.DBservice.DBServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);

    @Test
    public void common() {
        try (DBService dbService = new DBServiceImpl()){
            tryLoadAndSaveUserDataSet(dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    @Test
    public void commonHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            tryLoadAndSaveUserDataSet(dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    @Test
    public void readByNameHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            tryLoadAndSaveUserDataSet2(dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    @Test
    public void readAllHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            tryLoadAndSaveUserDataSet(dbService);
            tryLoadAndSaveUserDataSet2(dbService);
            List<UsersDataSet> dataSets = dbService.readAll(UsersDataSet.class);
            for (UsersDataSet userDataSet : dataSets) {
                System.out.println("Read all: "+userDataSet);
            }
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    private void tryLoadAndSaveUserDataSet(DBService dbService) {
        try {
            UsersDataSet user1 = new UsersDataSet(
                    "tully",
                    Collections.singletonList(new AddressDataSet("Mira")),
                    new PhoneDataSet("+1 234 567 8018"),
                    new PhoneDataSet("+7 987 645 4545")
            );
            dbService.save(user1);
            System.out.println("ОБъект отправлен в БД: " + user1);
            UsersDataSet user1Loaded = dbService.read(1, UsersDataSet.class);
            System.out.println("ОБъект извлечен из БД: " +user1Loaded);
            Assertions.assertEquals(user1, user1Loaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryLoadAndSaveUserDataSet2(DBService dbService) {
        try {
            List<AddressDataSet> addresses = new ArrayList<>();
            addresses.add(new AddressDataSet("Truda"));
            addresses.add(new AddressDataSet("Moskovskaya"));
            UsersDataSet user2 = new UsersDataSet(
                    "sully",
                    addresses,
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



}
