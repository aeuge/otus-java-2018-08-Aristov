package ru.otus.ATM;

public class AtmWithdrawException extends Exception {
    String name;
    AtmWithdrawException (String name) {
        this.name = name;
    }

    public String getName (){
        return name;
    }
}
