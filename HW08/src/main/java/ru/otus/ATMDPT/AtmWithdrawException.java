package ru.otus.ATMDPT;

public class AtmWithdrawException extends Exception {
    String message;

    AtmWithdrawException (String message) {
        this.message = message;
    }

    public String getMessage (){
        return super.getMessage();
    }
}
