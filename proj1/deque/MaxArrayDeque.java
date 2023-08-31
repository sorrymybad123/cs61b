package deque;

import java.util.Comparator;

import static java.lang.Math.round;
import static org.junit.Assert.assertEquals;

public class MaxArrayDeque<Item> extends ArrayDeque<Item>  {

    Comparator<Item> c;
 /* create a MaxArrayDeque with the given Comparator */
    public MaxArrayDeque(Comparator<Item> c) {
        super();
        this.c = c;
    }

/* returns the maximum element in the deque
as governed by the previously given Comparator.
If the MaxArrayDeque is empty, simply return null. */
    public Item max() {
        Item maxElement = this.getFirst();
        for (Item x : this) {
            if (c.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }
/* returns the maximum element in the deque
as governed by the parameter Comparator c.
If the MaxArrayDeque is empty, simply return null.*/
    public Item max(Comparator<Item> c){
        Item maxElement = this.getFirst();
        for (Item x : this) {
            if (c.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }

    private static class NameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }


    public static NameComparator getNameComparator(){
        return new NameComparator();
    }


    private static class NumberComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }
    public static Comparator<Integer> getNumberComparator() {
        return new NumberComparator();
    }
    public static <T> MaxArrayDeque<T> of(Comparator<T> c, T... stuff){
        MaxArrayDeque<T> returnArray = new MaxArrayDeque<>(c);
        for (T x : stuff){
            returnArray.addLast(x);
        }
        return returnArray;
    }
   public static void main(String[] args) {
        MaxArrayDeque<Integer> MAQ = new MaxArrayDeque(MaxArrayDeque.getNumberComparator());
        System.out.println(MAQ.max());
        MaxArrayDeque<String> MAQ2 = MaxArrayDeque.of(MaxArrayDeque.getNameComparator(), "z", "e", "f");
        System.out.println(MAQ2.max());


   }

}

