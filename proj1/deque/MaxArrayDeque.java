package deque;

import java.util.Comparator;


public class MaxArrayDeque<T> extends ArrayDeque<T>  {

    private Comparator<T> c;
 /* create a MaxArrayDeque with the given Comparator */
    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

/* returns the maximum element in the deque
as governed by the previously given Comparator.
If the MaxArrayDeque is empty, simply return null. */
    public T max() {
        T maxElement = this.get(0);
        for (T x : this) {
            if (c.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }
/* returns the maximum element in the deque
as governed by the parameter Comparator c.
If the MaxArrayDeque is empty, simply return null.*/
    public T max(Comparator<T> ca) {
        T maxElement = this.get(0);
        for (T x : this) {
            if (ca.compare(x, maxElement) > 0) {
                maxElement = x;
            }
        }
        return maxElement;
    }

}

