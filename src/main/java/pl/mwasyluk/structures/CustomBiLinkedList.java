package pl.mwasyluk.structures;

import java.util.Arrays;
import java.util.Objects;

public class CustomBiLinkedList<T> {
    BiNode<T> first;
    BiNode<T> last;
    int fixedLength = 0;

    public void add(T element){
        if (last == null){
            first = last = new BiNode<>(null, element, null);
        } else {
            BiNode<T> newLast = new BiNode<>(last, element, null);
            last.next = newLast;
            last = newLast;
        }
        fixedLength++;
    }

    private void assertIndexInBounds(int index){
        if (index < 0 || index >= fixedLength){
            throw new IndexOutOfBoundsException();
        }
    }

    private BiNode<T> getNode(int index){
        assertIndexInBounds(index);

        BiNode<T> node;
        if (index < fixedLength >> 1){
            node = first;
            for (int i = 1; i <= index; i++){
                node = node.next;
            }
        } else {
            node = last;
            for (int i = fixedLength - 2; i >= index; i--){
                node = node.previous;
            }
        }

        return node;
    }

    public T get(int index){
        BiNode<T> node = getNode(index);

        return node.element;
    }

    public void set(int index, T element){
        BiNode<T> node = getNode(index);

        node.element = element;
    }

    public void remove(int index){
        BiNode<T> node = getNode(index);

        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            first = node.next;
        }

        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            last = node.previous;
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
        int index = 0;
        if (element == null){
            for (BiNode<T> n = first; n != null; n = n.next, index++){
                if (n.element == null){
                    return index;
                }
            }
        } else {
            for (BiNode<T> n = first; n != null; n = n.next, index++){
                if ( element.equals(n.element)){
                    return index;
                }
            }
        }

        return -1;
    }

    public Object[] toArray(){
        Object[] array = new Object[fixedLength];

        int i = 0;
        for (BiNode<T> n = first; n != null; n = n.next, i++){
            array[i] = n.element;
        }

        return array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomBiLinkedList<?> that = (CustomBiLinkedList<?>) o;
        return fixedLength == that.fixedLength && Objects.equals(first, that.first) && Objects.equals(last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last, fixedLength);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    private static class BiNode<T> {
        BiNode<T> previous;
        T element;
        BiNode<T> next;

        public BiNode(BiNode<T> previous, T element, BiNode<T> next) {
            this.previous = previous;
            this.element = element;
            this.next = next;
        }
    }
}
