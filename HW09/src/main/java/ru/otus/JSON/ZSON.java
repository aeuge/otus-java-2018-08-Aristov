package ru.otus.JSON;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class ZSON {
    private static Logger logger = LoggerFactory.getLogger(ZSON.class);

    public static String convertObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        if (obj!=null) {
            Class<?> cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            List<String> resultList = new ArrayList<>();

            for (Field f : fields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                if (!isTransient) {
                    f.setAccessible(true);
                    if (f.get(obj) != null) {
                        resultList.add( "\"" + f.getName() + "\":" + convertSingleObjectToJSON(f.get(obj)));
                    }
                }
            }
            return "{"+String.join(",",resultList) + "}";
        } else {
            return "";
        }
    }

    private static String convertSingleObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        if (isMap(obj)) {
            return convertMapObjectToJSON(obj);
        } else if (isCollection(obj)) {
            return convertCollectionObjectToJSON(obj);
        } else if (obj.getClass() instanceof Class<?>) {
            if (obj.getClass().isArray()) {
                return convertArrayObjectToJSON(obj);
            } else {
                return convertPrimitiveObjectToJSON(obj);
            }
        } else {
            throw new ClassNotFoundException();
        }
    }

    private static String convertPrimitiveObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        if (isNumberOrBoolean(obj)) {
            return obj.toString();
        } else if (isCharacter(obj)) {
            return "\"" + obj.toString() + "\"";
        } else {
            return convertObjectToJSON(obj);
        }
    }

    private static String convertArrayObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        int arrayLength = Array.getLength(obj);
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < arrayLength; i++) {
            resultList.add(convertSingleObjectToJSON(Array.get(obj, i)));
        }
        return  "["+ String.join(",",resultList) +"]";
    }

    private static String convertCollectionObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        List<String> resultList = new ArrayList<>();
        Iterator iterator = ((Collection) obj).iterator();
        while (iterator.hasNext()){
            resultList.add(convertSingleObjectToJSON(iterator.next()));
        }
        return  "["+ String.join(",",resultList) +"]";
    }

    private static String convertMapObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        List<String> resultList = new ArrayList<>();
        Map myMap = new HashMap((Map) obj);
        myMap.forEach((k,v)-> {
            try {
                resultList.add(addQuotas(convertSingleObjectToJSON(k))+":"+addQuotas(convertSingleObjectToJSON(v)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return "{"+String.join(",",resultList)+"}";
    }

    private static String addQuotas(String s) {
        return s.charAt(0)!='\"' ? "\""+s+"\"" : s;
    }

    public static boolean isMap(Object ob) {
        return ob instanceof Map ;
    }

    public static boolean isCollection(Object ob) {
        return ob instanceof Collection ;
    }

    public static boolean isCharacter(Object ob) {
        return (ob instanceof Character)||(ob instanceof String);
    }
    public static boolean isNumberOrBoolean (Object ob) {
        return (ob instanceof Integer) || (ob instanceof Float) || (ob instanceof Double) || (ob instanceof Short) || (ob instanceof Long) || (ob instanceof Byte) || (ob instanceof Boolean);
    }
}
