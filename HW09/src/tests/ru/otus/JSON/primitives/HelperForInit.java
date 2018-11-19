package ru.otus.JSON.primitives;

import java.util.*;

public class HelperForInit {

    public static BagOfPrimitives getTestBagOfPrimitives() {

        List<String> listOfStrings = new ArrayList<>();
        Set<Integer> setOfIntegers = new HashSet<>();
        Map<Integer, String> mapOfBooleans = new HashMap<>();
        List<List<String>> nestedListOfStrings = new ArrayList<>();
        listOfStrings.add("first string");
        listOfStrings.add("second string");
        setOfIntegers.add(1);
        setOfIntegers.add(100);
        mapOfBooleans.put(1,"this is false");
        mapOfBooleans.put(2,"this is true");
        nestedListOfStrings.add(listOfStrings);
        nestedListOfStrings.add(listOfStrings);

        return new BagOfPrimitives(22,"test",new int[] { 1, 2, 3, 4, 5},10,100,3.7d,false,new InnerBag(9,"Inner bag"),listOfStrings,nestedListOfStrings,setOfIntegers,mapOfBooleans);
    }
}
