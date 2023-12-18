package pl.mwasyluk.sorting;

public class BubbleSort extends Sorter {

    @Override
    protected void execute_sorting(int[] tab) {
        // Bubble Sort places one unsorted element (the bigger one) in the correct place after
        // each iteration.
        for (int i = 0; i < tab.length - 1; i++){
            boolean anyChange = false;
            // Each iteration compares every pair of unsorted elements placed next to each other.
            for (int j = 0; j < tab.length - i - 1; j++ ){
                // When a bigger element is placed in front of a smaller one, they are swapped.
                if (tab[j] > tab[j+1]){
                    int x = tab[j];
                    tab[j] = tab[j+1];
                    tab[j+1] = x;
                    anyChange = true;
                }
            }
            // No changes in the array after any iteration means the array is already sorted.
            if (!anyChange){
                return;
            }
        }
    }
}
