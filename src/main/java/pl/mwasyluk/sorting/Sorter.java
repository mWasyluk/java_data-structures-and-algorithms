package pl.mwasyluk.sorting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class Sorter {
    private final Logger log = LoggerFactory.getLogger(Sorter.class);

    // Allows client to sort array with single visible method
    // Displays original array and starts the timer
    public final void sort(int[] tab){
        log.info("Starting sorting: " + Arrays.toString(tab) + ", strategy: " +
                this.getClass().getSimpleName());

        long startTime = System.currentTimeMillis();
        execute_sorting(tab);
        long endTime = System.currentTimeMillis();

        log.info("Array " + Arrays.toString(tab) +
                " has been sorted in " + (endTime - startTime) + " millis");
    }

    // Forces subclasses to provide sort algorithm
    abstract protected void execute_sorting(int[] tab);
}
