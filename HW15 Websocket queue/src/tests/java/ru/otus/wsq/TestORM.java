package ru.otus.war;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.wsq.app.DBService;
import ru.otus.wsq.dataset.AddressDataSet;
import ru.otus.wsq.dataset.PhoneDataSet;
import ru.otus.wsq.dataset.UsersDataSet;

import java.sql.SQLException;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:springBeans.xml")
@DisplayName("Тестирование DAO пользователей")
public class TestORM {
    private static Logger logger = LoggerFactory.getLogger(TestORM.class);
    @Autowired
    private DBService dbService;

    @Test
    @DisplayName("должен сохранить, а потом загрузить данные по id пользователя")
    public void commonHibernate() {
        UsersDataSet user1 = new UsersDataSet(
                "tully2",
                new AddressDataSet("Mira"),
                new PhoneDataSet("+1 234 567 8018"),
                new PhoneDataSet("+7 987 645 4545")
        );
        dbService.save(user1);
        System.out.println("ОБъект отправлен в БД: " + user1);
        UsersDataSet user1Loaded = null;
        try {
            user1Loaded = dbService.read(3, UsersDataSet.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ОБъект извлечен из БД: " + user1Loaded);
        Assertions.assertEquals(user1, user1Loaded);
    }

    @Test
    @DisplayName("должен сохранить, а потом загрузить данные по имени пользователя")
    public void readByNameHibernate() {
        UsersDataSet user2 = new UsersDataSet(
                "sully2",
                new AddressDataSet("Truda"),
                new PhoneDataSet("+67 890 344 4422")
        );
        dbService.save(user2);
        UsersDataSet user2Loaded = dbService.readByName("sully2", UsersDataSet.class);
        System.out.println(user2Loaded);
        Assertions.assertEquals(user2, user2Loaded);
    }

}
