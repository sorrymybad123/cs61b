package deque;

import java.util.Iterator;
import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> Ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", Ad1.isEmpty());
        Ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, Ad1.size());
        assertFalse("Ad1 should now contain 1 item", Ad1.isEmpty());

        Ad1.addLast("middle");
        assertEquals(2, Ad1.size());


        Ad1.addLast("back");
        assertEquals(3, Ad1.size());

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        // should be empty
        assertTrue("Ad1 should be empty upon initialization", Ad1.isEmpty());

        Ad1.addFirst(10);
        // should not be empty
        assertFalse("Ad1 should contain 1 item", Ad1.isEmpty());

        Ad1.removeFirst();
        // should be empty
        assertTrue("Ad1 should be empty after removal", Ad1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(3);

        Ad1.removeLast();
        Ad1.removeFirst();
        Ad1.removeLast();
        Ad1.removeFirst();

        int size = Ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    public void multipleParamTest() {

        ArrayDeque<String>  Ad1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        Ad1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = Ad1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, Ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, Ad1.removeLast());

    }

    /* test the iterator fuction is right*/
    @Test
    public void IteratorTest() {
        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(2);
        Ad1.addFirst(1);
        Ad1.addLast(3);
        Iterator<Integer> Ad1Iterator = Ad1.iterator();
        int i = 1;
        while(Ad1Iterator.hasNext()){
            int j = Ad1Iterator.next();
            assertEquals(i, j);
            i++;
        }
    }

    @Test
    public void getTest(){
        ArrayDeque<Integer> Ad1 = new ArrayDeque<>();
        Ad1.addFirst(2);
        Ad1.addFirst(1);
        Ad1.addLast(3);
        assertEquals(1, (int)Ad1.get(0));
        assertEquals(3, (int)Ad1.get(2));
    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        ArrayDeque<Integer> Ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            Ad1.addLast(i);
        }


        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) Ad1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) Ad1.removeLast(), 0.0);
        }

    }

    @Test
    public void testEqual(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        ArrayDeque<Integer> b = new ArrayDeque<>();
        assertTrue(a.equals(b));
        a.addLast(1);
        assertFalse(a.equals(b));
        a.addLast(2);
        a.addLast(3);
        b.addLast(1);
        b.addLast(2);
        b.addLast(3);
        assertTrue(a.equals(b));
        ArrayDeque<Integer> c = new ArrayDeque<>();
        c.addLast(1);
        c.addLast(2);
        c.addLast(4);
        assertFalse(a.equals(c));
    }
}


