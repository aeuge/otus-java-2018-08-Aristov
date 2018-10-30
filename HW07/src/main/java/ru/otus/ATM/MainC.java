package ru.otus.ATM;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainC {
    private static ATM myATM = new ATM();
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    public static void main(String[] args) throws Exception {

        try {
            loadCash();
            logger.info("Загружаем в банкомат деньги:");
            logger.info("Деньги в банкомат успешно загружены");
            logger.info("Всего денег в банкомате: "+myATM.getStringTotal());
            logger.info("Добавим денег:");
            myATM.addCashbox(200,10);
            logger.info("Всего в банкомате: "+myATM.getStatus());
            withdraw(-1);
            withdraw(9301);
            withdraw(9300);
            logger.info("Всего в банкомате: "+myATM.getStatus());
        }
        catch (AtmLoadException e) {
            logger.info("ошибка"+e.getName());
        }

    }

    private static void withdraw (int total) {
        try {
            myATM.withdraw(total);
            logger.info("выдача успешна: "+total);
        } catch (AtmWithdrawException e) {
            logger.info("ошибка выдачи: "+e.getName());
        }
    }

    private static int loadCash () throws AtmLoadException {
        try {
            myATM.addCashbox(2000,1);
            myATM.addCashbox(5000,100);
            myATM.addCashbox(1000,100);
            myATM.addCashbox(500,100);
            myATM.addCashbox(200,100);
            myATM.addCashbox(100,100);
            return 1;
        } catch (Exception e) {
            logger.info("ошибка загрузки денег в банкомат: "+e.getMessage());
            throw new AtmLoadException(e.getMessage());
        }
    }
}
