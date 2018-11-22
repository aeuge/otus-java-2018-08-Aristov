package ru.otus.ORM;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ORM.helper.CreateTableORM;
import ru.otus.ORM.helper.UsersDataSet;
import ru.otus.ORM.main.Executor;

public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);

    @Test
    public void commonOrmTest() {
        try (Executor exec = new Executor()){
            CreateTableORM.generateDefaultTable(exec.getConnection());
            UsersDataSet uds = new UsersDataSet(3, "kitty", 5);
            exec.save(uds);
            UsersDataSet uds2 = new UsersDataSet(23, "sunny", 10);
            exec.save(uds2);
            UsersDataSet uds3 = new UsersDataSet(43, "funny", 20);
            exec.save(uds3);
            UsersDataSet uds4 = exec.load(3,UsersDataSet.class);
            System.out.println("В БД с ID=3 есть запись: "+uds4);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }


}
