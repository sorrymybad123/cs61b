package deque;

import static org.junit.Assert.*;
import org.junit.Test;

public class DequeTest {

    @Test
    public void testAddFirstAndRemoveFirst() {
        Deque<String> deque = new LinkedListDeque<>();
        deque.addFirst("A");
        deque.addFirst("B");
        assertEquals("B", deque.removeFirst());
        assertEquals("A", deque.removeFirst());
    }

    @Test
    public void testAddLastAndRemoveLast() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        assertEquals(2, (int) deque.removeLast());
        assertEquals(1, (int) deque.removeLast());
    }

    @Test
    public void testIsEmpty() {
        Deque<Integer> deque = new ArrayDeque<>();
        assertTrue(deque.isEmpty());
        deque.addFirst(1);
        assertFalse(deque.isEmpty());
        deque.removeFirst();
        assertTrue(deque.isEmpty());
    }

    @Test
    public void testSize() {
        Deque<String> deque = new LinkedListDeque<>();
        assertEquals(0, deque.size());
        deque.addFirst("A");
        assertEquals(1, deque.size());
        deque.addLast("B");
        assertEquals(2, deque.size());
        deque.removeFirst();
        assertEquals(1, deque.size());
    }

    @Test
    public void testGet() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        assertEquals(1, (int) deque.get(0));
        assertEquals(2, (int) deque.get(1));
    }

    @Test
    public void testGetRecursive() {
        Deque<Integer> deque = new LinkedListDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        assertEquals(1, (int) ((LinkedListDeque<Integer>) deque).getRecursive(0));
        assertEquals(2, (int) ((LinkedListDeque<Integer>) deque).getRecursive(1));
    }


    @Test
    public void testEquals() {
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        linkedListDeque.addLast(1);
        linkedListDeque.addLast(2);

        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast(1);
        arrayDeque.addLast(2);

        assertTrue(linkedListDeque.equals(arrayDeque)); // 应该返回true
    }
}
