package ru.otus.war.db;

import org.springframework.stereotype.Component;
import ru.otus.war.DBservice.DBService;
import ru.otus.war.dataset.*;

@Component
public class DBDataInitializer {
    public DBDataInitializer(DBService dbService) {
        try {
            UsersDataSet user1 = new UsersDataSet(
                    "tully",
                    new AddressDataSet("Mira"),
                    new PhoneDataSet("+1 234 567 8018"),
                    new PhoneDataSet("+7 987 645 4545")
            );
            dbService.save(user1);
            UsersDataSet user2 = new UsersDataSet(
                    "sully",
                    new AddressDataSet("Lenina"),
                    new PhoneDataSet("+951 295 50 50"),
                    new PhoneDataSet("+7 987 111 0000")
            );
            dbService.save(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
