package deque;

import java.util.Iterator;

public interface Deque<T> extends Iterable<T>{

    void addFirst(T item);
    void addLast(T item);
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);

    Iterator<T> iterator();

    boolean equals(Object o);


    default boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }
}
