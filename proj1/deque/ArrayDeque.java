package deque;

import java.util.Iterator;
import java.util.Objects;

import static java.lang.Math.round;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {

    private int size;
    private int nextFirst;
    private T[] items;
    private int nextLast;
    private static int startNumber = 4;
    private static int endNumber = 5;

    public ArrayDeque() {
        nextFirst = startNumber;
        items = (T[]) new Object[8];
        nextLast = endNumber;
        size = 0;
    }

    public void addLast(T i) {
        if (this.size >= items.length) {
            this.resize(2 * items.length);
        }
        items[nextLast] = i;
        size += 1;
        nextLast = (nextLast + 1) % items.length;
    }




    public void addFirst(T i) {
        if (this.size >= items.length) {
            this.resize(2 * items.length);
        }
        items[nextFirst] = i;
        nextFirst = nextFirst - 1;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
        size += 1;
    }



    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();

        }
        return items[(index + nextFirst + 1) % items.length];
    }


    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int j = 0;
        //make all the old values move to the new array
        Iterator<T> itemIterator = this.iterator();
        while (itemIterator.hasNext()) {
            a[j] = itemIterator.next();
            j++;
        }
        items = a;
        //set the nextFirst value as last.
        nextFirst = items.length - 1;
        nextLast = j;
    }


    @Override
    public T removeFirst() {
        if (!this.isEmpty()) {
            nextFirst = (nextFirst + 1) % items.length;
            T whatever = items[nextFirst];
            items[nextFirst] = null;
            size -= 1;
            // resize down after remove
            if (size <= items.length / 4 && items.length >= 16) {
                resize(round(items.length / 2));
            }
            return whatever;
        }
        return null;
    }
    @Override
    public T removeLast() {
        if (!this.isEmpty()) {
            nextLast = nextLast - 1;
            if (nextLast < 0) {
                nextLast = items.length - 1;
            }
            T whatever = items[nextLast];
            items[nextLast] = null;
            size -= 1;
            if (size <= items.length / 4 && items.length >= 16) {
                resize(round(items.length / 2));
            }
            return whatever;
        }
        return null;
    }


    public int size() {
        return size;
    }




    @Override
    public Iterator<T> iterator() {
        return new ArrayDeuqeIterator();
    }


    private class ArrayDeuqeIterator implements Iterator<T> {
        private int wizPos;
        private int insideSize;

        ArrayDeuqeIterator() {
            wizPos = (nextFirst + 1) % items.length;
            insideSize = size;
        }
        @Override
        public boolean hasNext() {
            return insideSize > 0;
        }

        @Override
        public T next() {
            T returnT = items[wizPos];
            wizPos = (wizPos + 1) % items.length;
            insideSize -= 1;
            return returnT;
        }

    }



    @Override
    public boolean equals(Object o) {
        //检查地址是否相同
        if (o == this) {
            return true; // 同一个对象
        }
        // 检查o的类型是否为ArrayDeque
        if (o instanceof Deque<?>) { // 是否有相同的泛型类型
            Deque<?> oas = (Deque<?>) o;
            // 检查两个对象的大小是否相等
            if (oas.size() != this.size()) {
                return false;
            }
            // 按顺序检查所有值相等
            Iterator<T> thisIterator = this.iterator();
            Iterator<?> otherIterator = oas.iterator();
            while (thisIterator.hasNext() && otherIterator.hasNext()) {
                T thisItem = thisIterator.next();
                Object otherItem = otherIterator.next();
                if (!(Objects.equals(thisItem, otherItem))) {
                    return false;
                }
            }
            return true;
        } else {
            return false; // 不是相同类型的对象
        }
    }





    public void printDeque() {
        System.out.println(this.toString());
    }

}

