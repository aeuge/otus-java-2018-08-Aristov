package ru.otus.ATM;

import java.util.Objects;

public class Cashbox {
    public Cashbox(int value, int count) {
        this.value = value;
        this.count = count;
    }

    public Cashbox(int value) {
        this.value = value;
    }

    public int getTotal() {
        return value*count;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void decCount(int count) {
        this.count -= count;
    }
    private int count=0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashbox cashbox = (Cashbox) o;
        return value == cashbox.value;
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
