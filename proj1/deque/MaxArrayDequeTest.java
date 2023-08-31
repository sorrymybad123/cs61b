package deque;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    /* test the max() fuction when the Deque is null */
    @Test
    public void TestNull() {
        MaxArrayDeque<Integer> MAQ = new MaxArrayDeque(MaxArrayDeque.getNumberComparator());
        assertEquals(null, MAQ.max());
    }


    @Test
    public void TestMax() {
        MaxArrayDeque<String> MAQ2 = MaxArrayDeque.of(MaxArrayDeque.getNameComparator(), "z", "e", "f");
        assertEquals("z", MAQ2.max());
        MaxArrayDeque<Integer> MAQ = MaxArrayDeque.of(MaxArrayDeque.getNumberComparator(),1, 2, 3, 100, 3, 1000, 2, 3);
        assertEquals(1000, (int) MAQ.max());
    }

}
