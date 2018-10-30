package ru.otus.MyArrayList;

import java.util.*;

public class MainC {
    public static void main(String[] args) {
        String[] s = {"elephant","cat","dog","frog","elephant","cat","dog","frog","elephant","cat","dog","frog"};
        ArrayList<String> al = new ArrayList<>();
        al.addAll(Arrays.asList(s));
        MyArrayList<String> mal = new MyArrayList<>(1);
        mal.add("1");
        mal.add("1");
        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");
        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");        mal.add("1");
        System.out.println("Размер MyArrayList массива: "+mal.size()+", а вместимость: "+mal.capacity()+", содержимое:"+mal);
        Collections.addAll(mal,"3", "4");
        System.out.println("Размер MyArrayList массива после Collections.AddAll: "+mal.size()+", а вместимость: "+mal.capacity()+", содержимое:"+mal);
        Collections.copy(mal,al);
        System.out.println("Размер MyArrayList массива после Collections.Copy: "+mal.size()+", а вместимость: "+mal.capacity()+", скопировано:"+mal);
        Collections.sort(mal,String::compareTo);
        System.out.println("Размер MyArrayList массива после Collections.Sort: "+mal.size()+", а вместимость: "+mal.capacity()+", отсортированный массив:"+mal);

    }



}
