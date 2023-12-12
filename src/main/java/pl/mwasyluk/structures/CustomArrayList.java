package pl.mwasyluk.structures;

import java.util.Arrays;
import java.util.Objects;

public class CustomArrayList<T>{
    public static final int DEFAULT_LENGTH = 10;

    private Object[] array;
    private int fixedLength = 0;

    public CustomArrayList(int initCap) {
        this.array = new Object[initCap];
    }

    public CustomArrayList() {
        this(DEFAULT_LENGTH);
    }

    private void adjustLength(int requiredLength){
        int currentLength = array.length;

        if (requiredLength <= currentLength){
            return;
        }

        int newLength = currentLength + (currentLength >> 1);
        if (newLength <= currentLength){
            throw new OutOfMemoryError("The array cannot be adjusted to the required length");
        }

        array = Arrays.copyOf(array, newLength);
    }

    public void add( T element ){
        adjustLength(fixedLength + 1);

        array[fixedLength++] = element;
    }

    private void assertIndexInBounds(int index){
        if (index < 0 || index >= fixedLength){
            throw new IndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    public T get( int index ){
        assertIndexInBounds(index);

        return (T) array[index];
    }

    public void set( int index, T element ){
        assertIndexInBounds(index);

        array[index] = element;
    }

    public void remove(int index ){
        assertIndexInBounds(index);

        if (index < fixedLength - 1){
            for (int i = index + 1; i < fixedLength; i ++){
                array[i - 1] = array[i];
            }
        }

        fixedLength--;
    }

    public int size(){
        return fixedLength;
    }

    public boolean isEmpty(){
        return fixedLength == 0;
    }

    public boolean contains( T element ){
        return indexOf(element) >= 0;
    }

    public int indexOf ( T element ){
        if (element == null){
            for (int i = 0; i < fixedLength; i ++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < fixedLength; i ++) {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    public Object[] toArray(){
        return Arrays.copyOf(array, fixedLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomArrayList<?> that = (CustomArrayList<?>) o;
        return fixedLength == that.fixedLength && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fixedLength);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "Capacity=" + array.length + ", " +  Arrays.toString(
                Arrays.stream(toArray())
                        .filter(Objects::nonNull)
                        .toArray());
    }
}
