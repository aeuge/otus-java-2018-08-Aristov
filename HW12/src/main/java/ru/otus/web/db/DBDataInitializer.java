package ru.otus.web.db;

import ru.otus.web.DBservice.DBService;
import ru.otus.web.dataset.AddressDataSet;
import ru.otus.web.dataset.PhoneDataSet;
import ru.otus.web.dataset.UsersDataSet;

public class DBDataInitializer {
    public static void initDbWithTestData(DBService dbService) {
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
