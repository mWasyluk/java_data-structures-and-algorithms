package pl.mwasyluk.sorting;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SorterTest {
    static Logger log = LoggerFactory.getLogger(SorterTest.class);
    static {
        Configurator.setRootLevel(Level.INFO);
    }

    Random random = new Random();
    int[] randomArray;
    int[] expectedArray;

    @BeforeEach
    void beforeEach(){
        randomArray = random.ints(10000, 0, 1000).toArray();
        expectedArray = Arrays.copyOf(randomArray, randomArray.length);
        Arrays.sort(expectedArray);
    }

    static Stream<Arguments> sortersGenerator() {
        return Stream.of(
                Arguments.of(Named.of("Bubble Sort", new BubbleSort())),
                Arguments.of(Named.of("Selection Sort", new SelectionSort())),
                Arguments.of(Named.of("Insertion Sort", new InsertionSort()))
        );
    }

    void sortWithTime(Sorter sorter, int[] array){
        sorter.sort(array);
        log.info(sorter.getClass().getSimpleName() + " executed in " +
                sorter.getExecutionTime() + " millis");
    }

    @ParameterizedTest
    @MethodSource("sortersGenerator")
    @DisplayName("sorts empty array")
    void sortsEmptyArray(Sorter sorter){
        randomArray = new int[]{};
        expectedArray = new int[0];

        sortWithTime(sorter, randomArray);

        assertArrayEquals(expectedArray, randomArray);
    }

    @ParameterizedTest
    @MethodSource("sortersGenerator")
    @DisplayName("does not change already sorted array")
    void doesNotChangeAlreadySortedArray(Sorter sorter){
        int[] expectedCopy = Arrays.copyOf(expectedArray, expectedArray.length);

        sortWithTime(sorter, expectedCopy);

        assertArrayEquals(expectedArray, expectedCopy);
    }

    @ParameterizedTest
    @MethodSource("sortersGenerator")
    @DisplayName("sorts random array")
    void sortsRandomArray(Sorter sorter){
        sortWithTime(sorter, randomArray);

        assertArrayEquals(expectedArray, randomArray);
    }

    @ParameterizedTest
    @MethodSource("sortersGenerator")
    @DisplayName("sorts reversed array")
    void sortsReversedArray(Sorter sorter){
        Stream<Integer> reversed = Arrays.stream(randomArray)
                .boxed()
                .sorted(Comparator.reverseOrder());
        randomArray = reversed.mapToInt(Integer::intValue).toArray();

        sortWithTime(sorter, randomArray);

        assertArrayEquals(expectedArray, randomArray);
    }
}
