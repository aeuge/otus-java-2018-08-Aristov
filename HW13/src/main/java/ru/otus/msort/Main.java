package ru.otus.msort;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static Integer LENGTH_OF_MAS = 10000;
    private static Integer[] mas = new Integer[LENGTH_OF_MAS];

    public static void main(String[] args) throws Exception {
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < LENGTH_OF_MAS; i++) {
            mas[i] = rnd.nextInt();
        }
        Integer[] mas_original = mas.clone();
        sort4();
        Arrays.sort(mas_original);
        if (Arrays.equals(mas_original,mas)) {
            logger.info("Многопоточная сортировка выполнена успешнo");
        } else {
            logger.info("FAIL");
        }
        //for (Integer m : mas) logger.info(m.toString());
    }

    public static void sort4() throws Exception {
        Thread t = new Thread(new SortThread());
        t.start();
        t.join();
        Thread t2 = new Thread(new SortThread());
        t2.start();
        t2.join();
        Thread t3 = new Thread(new SortThread());
        t3.start();
        t3.join();
        Thread t4 = new Thread(new SortThread());
        t4.start();
        t4.join();
    }

    public static class SortThread implements Runnable {

        public void run() {
            Boolean finish = false;
            Integer temp_integer;
            while (!finish) {
                finish = true;
                for (int i = 0; i < LENGTH_OF_MAS-1; i++) {
                    if (mas[i]>mas[i+1]) {
                       finish = false;
                       temp_integer = mas[i];
                       mas[i] = mas[i+1];
                       mas[i+1] = temp_integer;
                    }
                }
            }
        }
    }
}
