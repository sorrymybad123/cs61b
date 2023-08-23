package deque;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.Math.round;
import static org.junit.Assert.assertEquals;

public class ArrayDeque<Item> implements Iterable<Item> {

    int size;
    int nextFirst;
    Item[] items;
    int nextLast;

    public ArrayDeque() {
        nextFirst = 4;
        items = (Item[]) new Object[8];
        nextLast = 5;
        size = 0;
    }

    public void addLast(Item i) {
        if (this.size >= items.length) {
            this.resize(2 * items.length);
        }
        items[nextLast] = i;
        size += 1;
        nextLast = (nextLast + 1) % items.length;
    }


    public void addFirst(Item i) {
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


    public Item get(int index) {
        return items[index];
    }


    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        int j = 0;
        //make all the old values move to the new array
        Iterator<Item> itemIterator = this.iterator();
        while (itemIterator.hasNext()) {
            a[j] = itemIterator.next();
            j++;
        }
        items = a;
        //set the nextFirst value as last.
        nextFirst = items.length - 1;
        nextLast = j;
    }


    public Item removeFirst() {
        if (!this.isEmpty()) {
            nextFirst = (nextFirst + 1) % items.length;
            Item whatever = items[nextFirst];
            items[nextFirst] = null;
            size -= 1;
            if (size < items.length / 4 && size > 16) {
                resize(round(items.length / 2));
            }
            return whatever;
        }
        return null;
    }


    public Item removeLast() {
        if (!this.isEmpty()) {
            nextLast = nextLast - 1;
            Item whatever = items[nextLast];
            items[nextLast] = null;
            size -= 1;
            if (size < items.length / 4 && size > 16) {
                resize(round(items.length / 2));
            }
            return whatever;
        }
        return null;

    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Iterator<Item> iterator() {
        return new ArrayDeuqeIterator();
    }

    private class ArrayDeuqeIterator implements Iterator<Item> {
        private int wizPos;
        private int insideSize;

        public ArrayDeuqeIterator() {
            wizPos = (nextFirst + 1) % items.length;
            insideSize = size;
        }

        public boolean hasNext() {
            return insideSize > 0;
        }

        public Item next() {
            Item returnItem = items[wizPos];
            wizPos = (wizPos + 1) % items.length;
            insideSize -= 1;
            return returnItem;
        }

    }

    public Item getFirst() {
        return get((nextFirst + 1) % items.length);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ArrayDeque oas) {
            if (oas.size() != this.size()) {
                return false;
            }
            Iterator<Item> oasIter = oas.iterator();
            Iterator<Item> thisIter = this.iterator();
            while (oasIter.hasNext()) {

                if (!(oasIter.next() == thisIter.next())) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ArrayDeque<String> a = new ArrayDeque<>();
        ArrayDeque<String> b = new ArrayDeque<>();
        b.addFirst("a");
        boolean ab1 = a.equals(b);
        a.addFirst("a");
        boolean ab2 = a.equals(b);
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addLast("c");
        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(2);
        Ad1.addFirst(1);
        Ad1.addLast(3);
        Iterator<Integer> Ad1Iterator = Ad1.iterator();
        int i = 1;
        while (Ad1Iterator.hasNext()) {
            int j = Ad1Iterator.next();
            assertEquals(i, j);
            i++;
        }
    }
}
