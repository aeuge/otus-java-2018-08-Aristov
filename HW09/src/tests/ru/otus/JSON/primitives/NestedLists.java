package ru.otus.JSON.primitives;

import java.util.ArrayList;
import java.util.List;

public class NestedLists {
    private List<List<String>> nestedListOfStrings= new ArrayList<>();

    public NestedLists(List<String> nestedListOfStrings) {
        this.nestedListOfStrings.add(nestedListOfStrings);
        this.nestedListOfStrings.add(nestedListOfStrings);
    }
}
