package ru.otus.ATM;

import java.util.Objects;

public class cashbox {
    public cashbox(int value, int count) {
        Value = value;
        Count = count;
    }

    public cashbox(int value) {
        Value = value;
    }

    public int getTotal() {
        return Value*Count;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    private int Value;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public void addCount(int count) {
        Count += count;
    }

    public void decCount(int count) {
        Count -= count;
    }
    private int Count=0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cashbox cashbox = (cashbox) o;
        return Value == cashbox.Value;
    }

    @Override
    public int hashCode() {

        return Objects.hash(Value);
    }
}
