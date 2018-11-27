package ru.otus.ORM.dataset;

public abstract class DataSet {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
