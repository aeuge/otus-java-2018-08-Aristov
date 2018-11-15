package ru.otus.JSON;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainC {
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    public static void main(String[] args) {
        try {
            BagOfPrimitives obj = new BagOfPrimitives(22, "test", 10);
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            logger.info(json);

            String myString = MyGsonClass(obj);
            logger.info(myString);
        }
        catch (Exception e) {
            logger.info("ошибка "+e.getMessage());
        }
    }

    private static String MyGsonObject (Object obj) throws ClassNotFoundException {
        String resultstr = "";

        if(obj.getClass() instanceof ParameterizedType) {
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
                declaration += getDeclaration(typeArg);
            }

            declaration += ">";
            declaration = declaration.replace('$', '.');
            return declaration;
        } else if(obj.getClass() instanceof Class<?>) {
            String genericResultType = obj.getClass().getGenericSuperclass().toString();
            String canonicalName = obj.getClass().getCanonicalName();

            logger.info(obj.getClass().getName() + "--" + canonicalName + "--" + genericResultType);
            if(obj.getClass().isArray()) {
                // arrays
                int arrayLength = Array.getLength(obj);
                List<String> resultList = new ArrayList<>();
                for (int i = 0; i < arrayLength; i++) {
                    resultList.add(Array.get(obj,i).toString());
                }
                return  "["+generateResultStringFromList(resultList,",")+"]";
            }
            else {
                // primitive and types without parameters (normal/standard types)
                if ((canonicalName.equals("java.lang.Boolean")) || (genericResultType.equals("class java.lang.Number"))) {
                    return obj.toString();
                } else {
                    return "\"" + obj.toString() + "\"";
                }
            }
        }
        else {
            throw new ClassNotFoundException();
        }
    }

    private static String MyGsonClass(Object obj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
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

    private static String getDeclaration(Type genericType) {
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
                declaration += getDeclaration(typeArg);
            }

            declaration += ">";
            declaration = declaration.replace('$', '.');
            return declaration;
        }
        else if(genericType instanceof Class<?>) {
            Class<?> clazz = (Class<?>) genericType;

            if(clazz.isArray()) {
                // arrays
                return clazz.getComponentType().getCanonicalName() + "[]";
            }
            else {
                // primitive and types without parameters (normal/standard types)
                return clazz.getCanonicalName();
            }
        }
        else {
            // e.g. WildcardTypeImpl (Class<? extends Integer>)
            return genericType.getTypeName();
        }
    }


}
