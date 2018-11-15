package ru.otus.JSON;

class BagOfPrimitives {
    public int value1;
    private final String value2;
    private final int[] value3 = new int[2];
    private Integer value4;
    private transient int value5;
    public double value6 = 2.6d;
    private boolean value7 = true;

    public BagOfPrimitives(int value1, String value2,  Integer value4) {
        this.value1 = value1;
        this.value2 = value2;
        value3[0] = 3;
        value3[1] = 5;
        this.value4 = value4;
        value5 = 0;
    }

    private BagOfPrimitives() {
        value1 = 1;
        value2 = "abc";
        value3[0] = 3;
        value3[1] = 5;
        value4 = 100;
        value5 = 0;
    }
}
