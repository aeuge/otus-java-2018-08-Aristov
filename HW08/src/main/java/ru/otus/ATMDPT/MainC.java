package ru.otus.ATMDPT;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainC {
    private static ATMDPT myATMDPT = new ATMDPT();
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    public static void main(String[] args) {

        try {
            ATM atmA = new ATM();
            myATMDPT.register(atmA);
            ATM atmB = new ATM();
            myATMDPT.register(atmB);
            ATM atmC = new ATM();
            myATMDPT.register(atmC);

            logger.info("Всего в банкоматах: "+myATMDPT.getTotal());

            withdraw(atmA, 11000);

            logger.info("Всего в банкоматах: "+myATMDPT.getTotal());

            myATMDPT.resetATM();
            logger.info("Сбросили банкоматы на заводские настройки ");

            logger.info("Всего в банкоматах: "+myATMDPT.getTotal());


        }
        catch (Exception e) {
            logger.info("ошибка"+e.getMessage());
        }

    }

    private static void withdraw (ATM atm, int total) {
        try {
            atm.withdraw(total);
            logger.info("выдача успешна: "+total);
        } catch (Exception e) {
            logger.info("ошибка выдачи: "+e.getMessage());
        }
    }


}
