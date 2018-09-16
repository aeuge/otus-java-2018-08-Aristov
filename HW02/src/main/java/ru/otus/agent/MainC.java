package ru.otus.agent;

import com.google.common.primitives.Primitives;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import static ru.otus.agent.MyAgent.getObjectSize;

//usage: java -javaagent:target/aristov02.jar -jar target/aristov02.jar
public class MainC {
    public static void main(String[] args) {
        int i = 1;
        printObjectSize(new ArrayList<String>());
        printObjectSize("string");
        printObjectSize(i);
        printObjectSize(new Integer[100]);
        printObjectSize(new A());
        printObjectSize(new int[100]);
    }

    public static int SizeOf (Object obj) {
        Class c = obj.getClass();
        int size = 0;
        if (obj instanceof Object[]) {
            System.out.println("массив");
            if (c.isPrimitive()) {
                System.out.println("простой тип "+c);
            } else {
                System.out.println("SubObject: "+String.format("%s, %s, size=%s", obj, c, getObjectSize(c)));
                size += (int) getObjectSize(c);
                size += (int) SizeOf(c);
            }
            size = (int)getObjectSize(c.getClass())*((Object[]) obj).length;
        } else {
            Field fields[] = c.getFields();
            System.out.println("всего полей: "+fields.length);
            for (Field item:fields) {
                if (item.getType().isPrimitive()) {
                    System.out.println("простой тип "+item.getType());
                } else {
                    System.out.println("SubObject:"+String.format("%s, %s, size=%s", item, item.getType(), getObjectSize(item.getType())));
                    size += (int) getObjectSize(item.getType());
                    size += (int) SizeOf(item.getType());
                }
            }
        }
        return size;
    }
    
    
    public static void printObjectSize(Object obj) {
        try {
            Class c = obj.getClass();
            int size = 0;
            size = (int) getObjectSize(obj);
            System.out.println("Object:"+String.format("%s, size=%s", c, size));
            size += SizeOf (obj);
            System.out.println("Итоговый размер объейта"+String.format(" %s, size=%s ",c, size));

        }
        catch(Exception e) {
            System.out.println("Exception: " + e) ;
        }

    }


}
