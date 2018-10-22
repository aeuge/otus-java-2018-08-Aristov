package ru.otus.annotations;


import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
//import com.google.common.reflect;

public class MainC {
    final static String introspectClass = "ru.otus.annotations.ExampleClass";
    final static String introspectPackage = "java.lang.reflect";

    public static void main(String[] args) throws Exception {

        testAnnotations2(introspectClass);

        /*final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith(introspectPackage)) {
                final Class<?> clazz = info.load();
                testAnnotations2(clazz.getName());
            }
        }*/

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        ImmutableSet<ClassPath.ClassInfo> classInfos = classPath.getTopLevelClasses(introspectPackage);

        System.out.println("всего классов "+classInfos.size());
        for (ClassPath.ClassInfo c : classInfos) {
            System.out.println("смотрим класс "+c.getName());
            testAnnotations2(c.getName());
        }

    }


	public static void testAnnotations2(String str) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
	    Class<?> cls = Class.forName(str);
        Method[] methods = cls.getDeclaredMethods();
        Object obj = cls.newInstance();
        ArrayList<Method> methodsBefore = new ArrayList();
        ArrayList<Method> methodsAfter = new ArrayList();
        ArrayList<Method> methodsTest = new ArrayList();

        for (Method m : methods) {
            System.out.println("смотрим метод "+m.getName());
            Annotation[] an = m.getAnnotations();
            for (Annotation a : an) {
                if (a instanceof Before) {
                    System.out.println("find Before");
                    if (m.getParameterCount()==0) methodsBefore.add(m);
                }
                if (a instanceof After) {
                    System.out.println("find After");
                    if (m.getParameterCount()==0) methodsAfter.add(m);
                }
                if (a instanceof ru.otus.annotations.Test) {
                    System.out.println("find Test");
                    if (m.getParameterCount()==0) methodsTest.add(m);
                }
            }
        }

        for (Method m : methodsTest) {
            try {
                for (Method mB : methodsBefore) {
                    mB.invoke(obj);
                }
                m.invoke(obj);
            } finally {
                for (Method mA : methodsAfter) {
                    mA.invoke(obj);
                }
            }


        }


	}


	
}
