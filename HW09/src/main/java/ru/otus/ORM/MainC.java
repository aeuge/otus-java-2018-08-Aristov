package ru.otus.ORM;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainC {
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    public static void main(String[] args) {

        try {

            logger.info("Сбросили банкоматы на заводские настройки ");

        }
        catch (Exception e) {
            logger.info("ошибка"+e.getMessage());
        }
    }




}
