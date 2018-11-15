package ru.otus.JSON;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class MainC {
    private static Logger logger = LoggerFactory.getLogger(MainC.class);
    transient static Object[] myData;

    public static void main(String[] args) {
        //тестирование в tests/TestJSON
    }

    private static String MyCollectionObject(Object obj) throws ClassNotFoundException, IllegalAccessException {
        //logger.info(obj.getClass().getName());
        List<String> resultList = new ArrayList<>();
        Iterator iterator = ((Collection) obj).iterator();
        while (iterator.hasNext()){
            resultList.add(convertObject(iterator.next()));
        }
        return  "["+generateResultStringFromList(resultList,",")+"]";
    }
    private static String MyMapObject(Object obj) throws ClassNotFoundException, IllegalAccessException {
        //logger.info(obj.getClass().getName());
        List<String> resultList = new ArrayList<>();
        Map myMap = new HashMap((Map) obj);
        myMap.forEach((k,v)-> {
            try {
                resultList.add(convertObject(k,"\"")+":"+convertObject(v,"\""));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return "{"+generateResultStringFromList(resultList,",")+"}";
    }

    private static String convertObject(Object v, String s) throws ClassNotFoundException, IllegalAccessException{
        String str = convertObject(v);
        if (str.charAt(0)!='\"') {
            str = s+str+s;
        }
        return str;
    }

    private static String MyGsonObject (Object obj) throws ClassNotFoundException, IllegalAccessException {
        String resultstr = "";
        Type genericType = obj.getClass();
        if(genericType instanceof ParameterizedType) {
            // types with parameters
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            String declaration = parameterizedType.getRawType().getTypeName();
            declaration += "<";

            Type[] typeArgs = parameterizedType.getActualTypeArguments();

            for(int i = 0; i < typeArgs.length; i++) {
                Type typeArg = typeArgs[i];

                if(i > 0) {
                    declaration += ", ";
                }

                // note: recursive call
                declaration += MyGsonObject(typeArg);
            }

            declaration += ">";
            declaration = declaration.replace('$', '.');
            return declaration;
        } else if(isMap(obj)) {
            return MyMapObject(obj);
        } else if(isCollection(obj)) {
            return MyCollectionObject(obj);
        } else if(obj.getClass() instanceof Class<?>) {
            String genericResultType = obj.getClass().getGenericSuperclass().toString();
            String canonicalName = obj.getClass().getCanonicalName();
            canonicalName = canonicalName.replace("java.lang.","");

            logger.info(obj.getClass().getName() + "--" + canonicalName + "--" + genericResultType);
            if(obj.getClass().isArray()) {
                // arrays
                int arrayLength = Array.getLength(obj);
                List<String> resultList = new ArrayList<>();
                for (int i = 0; i < arrayLength; i++) {
                    resultList.add(convertObject(Array.get(obj,i)));
                }
                return  "["+generateResultStringFromList(resultList,",")+"]";
            }
            else {
                // primitive and types without parameters (normal/standard types)
                return convertObject(obj);
            }
        }
        else {
            throw new ClassNotFoundException();
        }
    }
    public static boolean isMap(Object ob) {
        return ob instanceof Map ;
    }

    public static boolean isCollection(Object ob) {
        return ob instanceof Collection ;
    }

    private static String convertObject(Object obj) throws ClassNotFoundException, IllegalAccessException {
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
            default: return MyGsonClass(obj);
        }
    }

    public static String MyGsonClass(Object obj) throws ClassNotFoundException, IllegalAccessException {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        String resultstr = "{";

        for (Field f : fields) {
            boolean isTransient = Modifier.isTransient(f.getModifiers());
            if (!isTransient) {
                //String resultType = getDeclaration(type);
                f.setAccessible(true);
                if (resultstr.length() > 1) {
                    resultstr += ",";
                }
                resultstr += "\"" + f.getName() + "\":"+MyGsonObject(f.get(obj));
           }
        }
        return resultstr+"}";
    }

    private static String generateResultStringFromList(List<String> resultList, String s) {
        String resultStr = resultList.get(0);
        for (int i = 1; i < resultList.size() ; i++) {
            resultStr += s+resultList.get(i);
        }
        return resultStr;
    }




}
