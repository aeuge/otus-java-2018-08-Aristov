package ru.otus.JSON;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.JSON.primitives.*;

import java.util.*;

public class TestJSON {
    private static Logger logger = LoggerFactory.getLogger(ZSON.class);

    @Test
    public void commonObjectTest() throws IllegalAccessException, ClassNotFoundException {
            BagOfPrimitives obj = HelperForInit.getTestBagOfPrimitives();
            Gson gson = new Gson();
            String gsonString = gson.toJson(obj);
            logger.info(gsonString);

            String myString = ZSON.convertClassToJSON(obj);
            logger.info(myString);
            Assertions.assertTrue(gsonString.equals(myString),"string NOT matches in commonObjectTest");
    }

    @Test
    public void arrayTest() throws IllegalAccessException, ClassNotFoundException {
        ArrayTesting arrayTesting = new ArrayTesting();
        arrayTesting.i[0][0] = 1;arrayTesting.i[0][1] = 2;arrayTesting.i[1][0] = 3;arrayTesting.i[1][1] = 4;

        Gson gson = new Gson();
        String gsonString = gson.toJson(arrayTesting);
        logger.info(gsonString);

        String myString = ZSON.convertClassToJSON(arrayTesting);
        logger.info(myString);
        Assertions.assertTrue(gsonString.equals(myString),"string NOT matches in arrayTest");
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

        String myString = ZSON.convertClassToJSON(nestedLists);
        logger.info(myString);
        Assertions.assertTrue(gsonString.equals(myString),"string NOT matches in nestedListTest");
    }
}
