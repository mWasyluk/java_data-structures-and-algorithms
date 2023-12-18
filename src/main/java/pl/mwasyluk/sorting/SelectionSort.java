package pl.mwasyluk.sorting;

public class SelectionSort extends Sorter {
    @Override
    protected void execute_sorting(int[] tab) {
        // Selection Sort selects the lowest unsorted element and swaps its position with the first
        // unsorted element (if it is not the same position).
        for (int i = 0; i < tab.length - 1; i++){
            // First unsorted element is treated as the temp. lowest value.
            int indexOfMin = i;
            // Each iteration compares the temp. lowest value with every other unsorted element
            // to determine index of the lowest element in the entire array.
            for (int j = i + 1; j < tab.length; j++){
                if (tab[j] < tab[indexOfMin] ){
                    indexOfMin = j;
                }
            }
            // When index of the determined lowest element is different from the first unsorted,
            // the elements are swapped.
            if (indexOfMin != i){
                int sup = tab[i];
                tab[i] = tab[indexOfMin];
                tab[indexOfMin] = sup;
            }
        }
    }
}
