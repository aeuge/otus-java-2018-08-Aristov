package ru.otus.JSON;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class ZSON {
    private static Logger logger = LoggerFactory.getLogger(ZSON.class);
    transient static Object[] myData;

    private static String convertCollectionObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        List<String> resultList = new ArrayList<>();
        Iterator iterator = ((Collection) obj).iterator();
        while (iterator.hasNext()){
            resultList.add(convertObjectToJSON(iterator.next()));
        }
        return  "["+ String.join(",",resultList) +"]";
    }

    private static String convertMapObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        List<String> resultList = new ArrayList<>();
        Map myMap = new HashMap((Map) obj);
        myMap.forEach((k,v)-> {
            try {
                resultList.add(addQuotas(convertObjectToJSON(k))+":"+addQuotas(convertObjectToJSON(v)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return "{"+String.join(",",resultList)+"}";
    }

    private static String addQuotas(String s) {
        String str = s;
        if (str.charAt(0)!='\"') {
            str = "\""+str+"\"";
        }
        return str;
    }

    private static String convertObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        //Type genericType = obj.getClass();
        //logger.info(obj.getClass().getName() + "++"+genericType );
        if (isMap(obj)) {
            return convertMapObjectToJSON(obj);
        } else if (isCollection(obj)) {
            return convertCollectionObjectToJSON(obj);
        } else if (obj.getClass() instanceof Class<?>) {
            if (obj.getClass().isArray()) {
                //logger.info("is array");
                int arrayLength = Array.getLength(obj);
                List<String> resultList = new ArrayList<>();
                for (int i = 0; i < arrayLength; i++) {
                    resultList.add(convertObjectToJSON(Array.get(obj, i)));
                }
                return "[" + String.join(",", resultList) + "]";
            } else {
                // primitive and types without parameters (normal/standard types)
                return convertInnerObjectToJSON(obj);
            }
        } else {
            throw new ClassNotFoundException();
        }
    }
    private static String convertInnerObjectToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        Class type = obj.getClass();
        if ((obj instanceof Integer)||(obj instanceof Float)||(obj instanceof Double)||(obj instanceof Short)||(obj instanceof Long)||(obj instanceof Byte)||(obj instanceof Boolean)) {
            return obj.toString();
        } else if ((obj instanceof Character)||(obj instanceof String)) {
            return "\"" + obj.toString() + "\"";
        } else {
            return convertClassToJSON(obj);
        }
    }

    private static String convertInnerObjectToJSON__(Object obj) throws ClassNotFoundException, IllegalAccessException {
        String canonicalName = obj.getClass().getCanonicalName();
        canonicalName = canonicalName.replace("java.lang.","");
        switch (canonicalName) {
            case "Integer":
            case "Float":
            case "Double":
            case "Short":
            case "Long":
            case "Byte":
            case "Boolean":  return obj.toString();
            case "Character":
            case "String":   return "\"" + obj.toString() + "\"";
            default: return convertClassToJSON(obj);
        }
    }


    public static String convertClassToJSON(Object obj) throws ClassNotFoundException, IllegalAccessException {
        if (obj!=null) {
            Class<?> cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            String resultstr = "{";

            for (Field f : fields) {
                boolean isTransient = Modifier.isTransient(f.getModifiers());
                if (!isTransient) {
                    f.setAccessible(true);
                    if (f.get(obj) != null) {
                        if (resultstr.length() > 1) {
                            resultstr += ",";
                        }
                        resultstr += "\"" + f.getName() + "\":" + convertObjectToJSON(f.get(obj));
                    }
                }
            }
            return resultstr + "}";
        } else {
            return "";
        }
    }

    public static boolean isMap(Object ob) {
        return ob instanceof Map ;
    }

    public static boolean isCollection(Object ob) {
        return ob instanceof Collection ;
    }
}
