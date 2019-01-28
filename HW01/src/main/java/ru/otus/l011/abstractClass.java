package ru.otus.l011;

public abstract class abstractClass {
    public int i;

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    private int b=0;

    public void aaa () {
        System.out.println(b);
        b = 1;
        System.out.println(b);
        b = 2;
        System.out.println(b);
    }
    public abstract void bbb ();
}

