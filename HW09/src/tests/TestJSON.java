import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.JSON.MainC;
import org.junit.Assert;

import java.util.*;

import static ru.otus.JSON.MainC.MyGsonClass;

public class TestJSON {
    private static Logger logger = LoggerFactory.getLogger(MainC.class);

    @Test
    public void commonObjectTest() throws IllegalAccessException, ClassNotFoundException {
            List<String> listOfStrings = new ArrayList<>();
            Set<Integer> setOfIntegers= new HashSet<>();
            Map<Integer, String> mapOfBooleans= new HashMap<>();
            List<List<String>> nestedListOfStrings= new ArrayList<>();
            listOfStrings.add("first string");
            listOfStrings.add("second string");
            setOfIntegers.add(1);
            setOfIntegers.add(100);
            mapOfBooleans.put(1,"this is false");
            mapOfBooleans.put(2, "this is true");
            nestedListOfStrings.add(listOfStrings);
            nestedListOfStrings.add(listOfStrings);

            BagOfPrimitives obj = new BagOfPrimitives(22, "test", new int[]{1,2,3,4,5},10,100, 3.7d,false,new InnerBag(9,"Inner bag"),listOfStrings,nestedListOfStrings,setOfIntegers,mapOfBooleans);
            Gson gson = new Gson();
            String gsonString = gson.toJson(obj);
            logger.info(gsonString);

            String myString = MyGsonClass(obj);
            logger.info(myString);
            if (gsonString.equals(myString)) {
                logger.info("string matches");
            } else {
                logger.info("string NOT matches");
            }
    }

    @Test
    public void nestedListTest() throws IllegalAccessException, ClassNotFoundException {
        List<String> listOfStrings = new ArrayList<>();
        listOfStrings.add("first string");
        listOfStrings.add("second string");
        NestedLists nestedLists = new NestedLists(listOfStrings);
        Gson gson = new Gson();
        String gsonString = gson.toJson(nestedLists);
        logger.info(gsonString);

        String myString = MyGsonClass(nestedLists);
        logger.info(myString);
        if (gsonString.equals(myString)) {
            logger.info("string matches");
        } else {
            logger.info("string NOT matches");
        }
    }
    @Test

    public void arrayTest() throws IllegalAccessException, ClassNotFoundException {
        int[][] i = new int[2][2];
        i[0][0] = 1;i[0][1] = 2;i[1][0] = 3;i[1][1] = 4;

        Gson gson = new Gson();
        String gsonString = gson.toJson(i);
        logger.info(gsonString);

        String myString = MyGsonClass(i);
        logger.info(myString);
        if (gsonString.equals(myString)) {
            logger.info("string matches");
        } else {
            logger.info("string NOT matches");
        }
    }

}
