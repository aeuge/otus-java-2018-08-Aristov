package ru.otus.hibernateORM;

import org.h2.tools.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernateORM.DBservice.DBService;
import ru.otus.hibernateORM.DBservice.DBServiceHibernateImpl;
import ru.otus.hibernateORM.dataset.UsersDataSet;
import ru.otus.hibernateORM.DBservice.DBServiceImpl;

import java.util.List;

public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);

    @Test
    public void common() {
        try (DBService dbService = new DBServiceImpl()){
            tryLoadAndSaveUserDataSet(3, "kitty", 5, dbService);
            tryLoadAndSaveUserDataSet(23, "sunny", 10, dbService);
            tryLoadAndSaveUserDataSet(43, "funny", 20, dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    @Test
    public void commonHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            tryLoadAndSaveUserDataSet(3, "kitty", 5, dbService);
            tryLoadAndSaveUserDataSet(5, "sunny", 10, dbService);
            tryLoadAndSaveUserDataSet(7, "funny", 20, dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    @Test
    public void readByNameHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            tryLoadAndSaveUserDataSetByName(54, "kitty", 5, dbService);
            tryLoadAndSaveUserDataSetByName(13, "sunny", 10, dbService);
            tryLoadAndSaveUserDataSetByName(32, "funny", 20, dbService);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }
    @Test
    public void readAllHibernate() {
        try (DBService dbService = new DBServiceHibernateImpl()){
            dbService.save(new UsersDataSet(54, "kitty", 5));
            dbService.save(new UsersDataSet(54, "swity", 50));
            dbService.save(new UsersDataSet(54, "manny", 15));
            List<UsersDataSet> l = dbService.readAll(UsersDataSet.class);
            System.out.println("Read All "+l.toString());
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    private void tryLoadAndSaveUserDataSet(long id, String name, int age, DBService dbService) {
        try {
            UsersDataSet uds = new UsersDataSet(id, name, age);
            System.out.println("ОБъект отправлен в БД: " + uds.toString());
            dbService.save(uds);
            UsersDataSet udl = dbService.read(uds.getId(), UsersDataSet.class);
            System.out.println("ОБъект извлечен из БД: " + udl);
            //Assertions.assertEquals(uds, udl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryLoadAndSaveUserDataSetByName(long id, String name, int age, DBService dbService) {
        try {
            UsersDataSet uds = new UsersDataSet(id, name, age);
            System.out.println("ОБъект отправлен в БД: " + uds.toString());
            dbService.save(uds);
            UsersDataSet udl = dbService.readByName(name, UsersDataSet.class);
            System.out.println("ОБъект извлечен из БД: " + udl);
            Assertions.assertEquals(uds, udl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
