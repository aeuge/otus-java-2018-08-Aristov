package ru.otus.ATM;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainC {
    private static ATMclass ATM = new ATMclass();
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    public static void main(String[] args) throws Exception {

        logger.info("Загружаем в банкомат деньги:");
        if (loadCash()==1) {
            logger.info("Деньги в банкомат успешно загружены");
            logger.info("Всего денег в банкомате: "+ATM.getStringTotal());
            logger.info("Добавим денег:");
            ATM.addCashbox(200,10);
            logger.info("Всего в банкомате: "+ATM.getStatus());
            logger.info("Пробуем выдать 9301: "+ATM.withdraw(9301));
            logger.info("Пробуем выдать 9300: "+ATM.withdraw(9300));
            logger.info("Пробуем выдать -1: "+ATM.withdraw(-1));
            logger.info("Всего в банкомате: "+ATM.getStatus());
        };
    }

    private static int loadCash () {
        try {
            ATM.addCashbox(2000,1);
            ATM.addCashbox(5000,100);
            ATM.addCashbox(1000,100);
            ATM.addCashbox(500,100);
            ATM.addCashbox(200,100);
            ATM.addCashbox(100,100);
            return 1;
        } catch (Exception e) {
            logger.info("ошибка загрузки денег в банкомат: "+e.getMessage());
            return 0;
        }
    }
}
