package ru.otus.ATMDPT;

public class AtmLoadException extends Exception {
    String message;

    AtmLoadException (String message) {
        this.message = message;
    }

    public String getMessage (){
        return super.getMessage();
    }
}
