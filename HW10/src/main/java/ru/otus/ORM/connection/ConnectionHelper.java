package ru.otus.ORM.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionHelper {

    static Connection getConnection() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());

            /**String url = "jdbc:mysql://" +       //db type
                    "localhost:" +               //host name
                    "3306/" +                    //port
                    "dbexample?" +              //db name
                    "user=tully&" +              //login
                    "password=tully&" +          //password
                    "useSSL=false";              //do not use Secure Sockets Layer
            */
            String url = "jdbc:h2:mem:";

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
