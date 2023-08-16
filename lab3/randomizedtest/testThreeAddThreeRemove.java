package randomizedtest;

import static org.junit.Assert.*;
import org.junit.Test;
import timingtest.AList;
import java.util.Random;
import edu.princeton.cs.algs4.*;


public class testThreeAddThreeRemove {
    public void addBoth(AListNoResizing<Integer> a, BuggyAList<Integer> b, int num){
        a.addLast(num);
        b.addLast(num);
    }
    @Test
    public void testThreeAddThreeRemove1(){
        AListNoResizing test1 = new AListNoResizing<Integer>();
        BuggyAList test1Buggy = new BuggyAList<Integer>();
        addBoth(test1, test1Buggy, 4);
        addBoth(test1, test1Buggy, 100);
        addBoth(test1, test1Buggy, 3000);
        assertEquals(3000,test1.removeLast());
        assertEquals(3000,test1Buggy.removeLast());
        assertEquals(100, test1.get(1));
        assertEquals(100, test1Buggy.get(1));



    }

    @Test
    public void ramdomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size_B = B.size();
                if (L.size() == B.size()) {
                }else {
                    break;
                }
            } else if (operationNumber == 2 && L.size() > 0) {
                int getNumber = L.getLast();
                int gerNumber_B = B.getLast();
                L.removeLast();
                B.removeLast();
                int size = L.size();
                int size_B = B.size();
                if (getNumber == gerNumber_B){
                } else{
                    break;
                }
                if (L.size() == B.size()) {
                }else {
                    break;
                }
            }
        }

    }


}