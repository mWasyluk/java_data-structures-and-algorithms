package pl.mwasyluk.sorting;

public abstract class Sorter {
    private int executionTime;

    // Allows client to sort array with single visible method
    // Displays original array and starts the timer
    public final void sort(int[] tab){
        long startTime = System.currentTimeMillis();
        execute_sorting(tab);
        long endTime = System.currentTimeMillis();
        this.setExecutionTime((int)(endTime - startTime));
    }

    // Forces subclasses to provide sort algorithm
    abstract protected void execute_sorting(int[] tab);

    private void setExecutionTime(int time){
        this.executionTime = time;
    }

    public final int getExecutionTime(){
        return this.executionTime;
    }
}
