package ru.otus.annotations;



public class ExampleClass {
    @Default(value="my text1", name="custom")
    private String text;
    @Default("noname")
    public String name;
    private int counter;

    public ExampleClass() {
    }

    @Before
    public void beforeClass() {
        System.out.println("Перед классом");
    }
    @After
    public void afterClass() {
        System.out.println("После класса");
    }
    @Test
    public void testClass() {
        System.out.println("Тест1");
    }

    @Test
    public void testClass2() {
        System.out.println("Тест 2");
    }

    public ExampleClass(String text, int counter) {
        super();
        this.text = text;
        this.counter = counter;
    }

    @MyAnnotation(name="print me!")
    public void printIt(){
        System.out.println("printIt() no param");
    }

    public void printItString(String temp){
        System.out.println("printIt() with param String : " + temp);
    }

    public void printItInt(int temp){
        System.out.println("printIt() with param int : " + temp);
    }

    public void setCounter(int counter){
        this.counter = counter;
        System.out.println("setCounter() set counter to : " + counter);
    }

    public void printCounter(){
        System.out.println("printCounter() : " + this.counter);
    }

}
