package ru.otus.agent;

import java.math.BigDecimal;
import java.util.*;
import static ru.otus.agent.MyAgent.getObjectSize;

//usage: java -javaagent:target/aristov02.jar -jar target/aristov02.jar
public class MainC {
    public static void main(String[] args) {
        printObjectSize(new Object());
        printObjectSize(new A());
        printObjectSize(1);
        printObjectSize("string");
        printObjectSize("огрооооомнаяяяяя строккааааааааааааа");
        printObjectSize(Calendar.getInstance());
        printObjectSize(new BigDecimal("999999999999999.999"));
        printObjectSize(new ArrayList<String>());
        printObjectSize(new Integer[100]);
    }

    public static void printObjectSize(Object obj) {
        System.out.println(String.format("%s, size=%s", obj.getClass()
                .getSimpleName(), getObjectSize(obj)));
    }


}
