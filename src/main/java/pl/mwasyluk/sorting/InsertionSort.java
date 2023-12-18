package pl.mwasyluk.sorting;

public class InsertionSort extends Sorter{
    @Override
    protected void execute_sorting(int[] tab) {
        // Insertion Sort creates an implicit array inside the original one and inserts there one
        // additional element in its appropriate position in each iteration.
        for (int i = 1; i < tab.length; i ++){
            // Each iteration swaps the element with previous ones until its value is lower.
            for (int j = i; j > 0 && tab[j] < tab[j-1] ; j --){
                int temp = tab[j];
                tab[j] = tab[j-1];
                tab[j-1] = temp;
            }
        }
    }
}
