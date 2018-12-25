package ru.otus.msort;

import java.util.ArrayList;
import java.util.List;

public class BubbleSortImpl implements BubbleSort {
    private static Integer[] mas;
    private static final int NUMER_OF_THREADS = 4;

    public static void sort(Integer[] a) throws InterruptedException {
        mas = a;
        List<Integer> arrayBounds = new ArrayList<>();
        int chunk = a.length / NUMER_OF_THREADS;
        arrayBounds.add(0);
        for (int i = 1; i < NUMER_OF_THREADS; i++) { arrayBounds.add(chunk*i); }
        arrayBounds.add(a.length);
        Thread[] threads = new Thread[NUMER_OF_THREADS];
        int indexThreads = 0;
        for (int i = 0; i < NUMER_OF_THREADS; i++) {
            int finalI = i;
            Thread t = new Thread(()->sortChunk(arrayBounds.get(finalI), arrayBounds.get(finalI + 1) - 1));
            threads[indexThreads++] = t;
            t.start();
        }
        for (int i = 0; i < NUMER_OF_THREADS; i++) {
            threads[i].join();
        }
        Thread t1 = new Thread(()->mergeResults(arrayBounds.get(0), arrayBounds.get(1)-1, arrayBounds.get(1), arrayBounds.get(2)-1));
        t1.start();
        Thread t2 = new Thread(()->mergeResults(arrayBounds.get(2), arrayBounds.get(3)-1, arrayBounds.get(3), arrayBounds.get(4)-1));
        t2.start();
        t2.join();
        t1.join();
        mergeResults(arrayBounds.get(0), arrayBounds.get(2)-1, arrayBounds.get(2), arrayBounds.get(4)-1);
    }

    public static void sortChunk(int lower, int upper) {
        for (int i = lower; i < upper; i++) {
            for (int j = lower; j < upper + lower - i; j++) {
                if (mas[j] > mas[j + 1]) {
                    Integer temp_integer = mas[j];
                    mas[j] = mas[j + 1];
                    mas[j + 1] = temp_integer;
                }
            }
        }
    }

    public static void mergeResults(int lowerA, int upperA,int lowerB, int upperB) {
        Integer[] tempMas = new Integer[upperA-lowerA+1+upperB-lowerB+1];
        int index = 0;
        int indexB = lowerB;
        int indexA = lowerA;
        while (indexA < upperA+1) {
            if (mas[indexA] < mas[indexB]) {
                tempMas[index++]=mas[indexA++];
            } else {
                tempMas[index++]=mas[indexB++];
            }
            if (indexB>upperB) {
                for (int j = indexA; j < upperA+1; j++) { tempMas[index++]=mas[j]; }
                break;
            }
        }
        while (indexB<upperB+1) { tempMas[index++]=mas[indexB++]; }
        index = 0;
        for (int i = lowerA; i < upperB+1; i++) {mas[i]=tempMas[index++];};
    }

    public static Boolean equals(Integer[] a,Integer[] b) {
        for (int i = 0; i < a.length; i++) {
            if (!(a[i].equals(b[i]))) { return false; }
        }
        return true;
    }
}
