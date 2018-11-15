import java.util.*;

class BagOfPrimitives {
    public int value1;
    private final String value2;
    private int[] value3 = new int[2];
    private Integer value4;
    private transient int value5;
    public double value6 = 2.6d;
    private boolean value7 = true;
    public InnerBag innerValue;
    private List<String> listOfStrings= new ArrayList<>();
    private transient List<List<String>> nestedListOfStrings= new ArrayList<>();
    private Set<Integer> setOfIntegers= new HashSet<>();
    private Map<Integer, String> mapOfBooleans= new HashMap<>();



    private BagOfPrimitives() {
        value1 = 1;
        value2 = "abc";
        value3[0] = 3;
        value3[1] = 5;
        value4 = 100;
        value5 = 0;
    }

    public BagOfPrimitives(int value1, String value2, int[] value3, Integer value4, int value5, double value6, boolean value7, InnerBag innerValue, List<String> listOfStrings, List<List<String>> nestedListOfStrings, Set<Integer> setOfIntegers, Map<Integer, String> mapOfBooleans) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.innerValue = innerValue;
        this.listOfStrings = listOfStrings;
        this.nestedListOfStrings = nestedListOfStrings;
        this.setOfIntegers = setOfIntegers;
        this.mapOfBooleans = mapOfBooleans;
    }
}
