package ru.otus.annotations;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
//import com.google.common.reflect;

public class MainC {
    final static String introspectClass = "ru.otus.annotations.ExampleClass";
    final static String introspectPackage = "com.google";
    final static String introspectPackage2 = "ru.otus.testanno";
    final static String introspectPackage4 = "java.lang.reflect";

    public static void main(String[] args) throws Exception {

        testAnnotations2(introspectClass);

        /*final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith(introspectPackage)) {
                final Class<?> clazz = info.load();
                testAnnotations2(clazz.getName());
            }
        }*/
        //first way inner package
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        ImmutableSet<ClassPath.ClassInfo> classInfos = classPath.getTopLevelClasses(introspectPackage);
        System.out.println("всего классов "+classInfos.size());

        //second
        List<Class<?>> classes = ClassFinder.find(introspectPackage2);
        System.out.println("всего классов 2 тип поиска: "+classes.size());
        for (Class c : classes) {
            System.out.println("смотрим класс "+c.getName());
            testAnnotations2(c.getName());
        }

        //третий вариант
        printClassCount("com.google",  ru.otus.annotations.MainC.class);

        //fourth way
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));

        List<Path> classes4 = Files
                .list(fs.getPath("modules", "java.base",introspectPackage4.replace(".","/")))
                .map(Path::getFileName)
                .filter(p -> p.toString().endsWith(".class") && !p.toString().contains("$"))
                .collect(Collectors.toList());
                //.forEach(System.out::println);
        System.out.println("всего классов 4 тип поиска in "+introspectPackage4+": "+classes4.size());
        for (Path c : classes4) {
            System.out.println("смотрим класс "+introspectPackage4+"."+c.toString().replace(".class",""));
            testAnnotations2(introspectPackage4+"."+c.toString().replace(".class",""));
        }
    }
    private static void printClassCount(String packageName, Class classForClassLoader) {
        System.out.println("Number of toplevel classes in " + packageName + ": " + countTopleveClassesInPackage(packageName, classForClassLoader));
    }

    private static int countTopleveClassesInPackage(String packageName, Class clazz) {
        try {
            ClassPath classPath = ClassPath.from(clazz.getClassLoader());
            return classPath.getTopLevelClassesRecursive(packageName).size();
        } catch (IOException e) {
            return 0;
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
