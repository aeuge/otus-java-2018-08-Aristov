package ru.otus.ATMDPT;

public class AtmLoadException extends Exception {
    String name;
    AtmLoadException (String name) {
        this.name = name;
    }

    public String getName (){
        return name;
    }
}
