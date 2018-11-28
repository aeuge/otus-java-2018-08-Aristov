package ru.otus.ORM;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ORM.db.DBStructureCreator;
import ru.otus.ORM.dataset.UsersDataSet;
import ru.otus.ORM.db.Executor;

public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);

    @Test
    public void commonOrmTest() {
        try (Executor exec = new Executor()){
            DBStructureCreator.generateDefaultTable(exec.getConnection());
            tryLoadAndSaveUserDataSet(3, "kitty", 5, exec);
            tryLoadAndSaveUserDataSet(23, "sunny", 10, exec);
            tryLoadAndSaveUserDataSet(43, "funny", 20, exec);
        } catch (Exception e) {
            logger.info("FAIL" + e.getMessage());
            Assertions.fail("FAIL");
        }
    }

    private void tryLoadAndSaveUserDataSet(long id, String name, int age, Executor exec) {
        try {
            UsersDataSet uds = new UsersDataSet(id, name, age);
            exec.save(uds);
            System.out.println("ОБъект отправлен в БД: " + uds.toString());
            UsersDataSet udl = exec.load(id,UsersDataSet.class);
            System.out.println("ОБъект извлечен из БД: " + udl);
            Assertions.assertEquals(uds, udl);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
