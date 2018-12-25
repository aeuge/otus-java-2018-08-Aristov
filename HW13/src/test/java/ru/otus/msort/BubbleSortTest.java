package ru.otus.msort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;

@DisplayName("Проверка многопоточной сортировки пузырьком")
public class BubbleSortTest {
    private static Logger logger = LoggerFactory.getLogger(BubbleSortImpl.class);
    private static Integer LENGTH_OF_MAS = 100_000;
    private Integer[] mas = new Integer[LENGTH_OF_MAS];

    @Test
    @DisplayName("успешно пройдена")
    public void BubbleSortTest() throws Exception {
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < LENGTH_OF_MAS; i++) {
            mas[i] = rnd.nextInt();
        }
        Integer[] mas_original = mas.clone();
        BubbleSortImpl.sort(mas);
        Arrays.sort(mas_original);
        Assertions.assertTrue(BubbleSortImpl.equals(mas_original,mas));
        logger.info("Многопоточная сортировка выполнена успешнo");
    }
}
