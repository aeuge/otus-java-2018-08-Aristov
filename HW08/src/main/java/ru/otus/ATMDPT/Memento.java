package ru.otus.ATMDPT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Memento {
    private List<Cashbox> cashboxes = new ArrayList<>();

    public Memento(List<Cashbox> cb) {
        for (Cashbox cashbox : cb) {
            cashboxes.add(new Cashbox(cashbox.getValue(),cashbox.getCount()));
        }
    }

    public List<Cashbox> getSavedState() {
        return cashboxes;
    }
}
