package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Iterable<Item>{

    int size;
    int nextFirst;
    Item[] items;
    int nextLast;

    public ArrayDeque(){
        nextFirst = 4;
        items = (Item[]) new Object[8];
        nextLast = 5;
        size = 0;
    }

    public void addLast(Item i){
        if (size == items.length){
            resize(2 * items.length);
        }
        items[nextLast] = i;
        size += 1;
        nextLast = (nextLast + 1) % items.length;
    }


    public void addFirst(Item i){
        if (size == items.length){
            resize(2 * items.length);
        }
        items[nextFirst] = i;
        nextFirst = nextFirst - 1;
        if (nextFirst < 0){
            nextFirst = items.length - 1;
        }
        size += 1;
    }




    public Item get(int index){
        return items[index];
    }



    private void resize(int capacity){
        Item[] a = (Item[]) new Object[capacity];
        int j = 0;
        for(int i = nextLast; i%items.length > nextFirst;i++){
            a[j] = get(i);
            j++;
        }
        nextFirst = items.length - 1;
        nextLast = j + 1;
    }

     public Item removeFirst() {
        int index = nextFirst + 1;
        Item whatever = items[index];
        nextFirst += 1;
        items[index] = null;
        size -= 1;
        if (size < items.length / 4 && size > 16){
            resize(items.length / 2);
        }
        return whatever;
     }

     public Item removeLast(){
        nextLast = nextLast - 1;
        Item whatever = items[nextLast];
        items[nextLast] = null;
        size -= 1;
         if (size < items.length / 4 && size > 16){
             resize(items.length / 2);
         }
         return whatever;
     }


     public int size(){
        return size;
     }


     public boolean isEmpty(){
         if (size == 0){
             return true;
         }else{
             return false;
         }
     }

     public  Iterator<Item> iterator(){
        return new ArrayDeuqeIterator();
     }

     private class ArrayDeuqeIterator implements Iterator<Item> {
        private int wizPos;

        public ArrayDeuqeIterator(){
            wizPos = (nextFirst + 1) % items.length;
        }

        public boolean hasNext(){
            return wizPos != nextLast;
        }

        public Item next(){
            Item returnItem = items[wizPos];
            wizPos = (nextFirst + 1) % items.length;
            return returnItem;

        }

         }
     public Item getFirst(){
        return get((nextFirst + 1) % items.length);
     }

     @Override
     public boolean equals(Object o){
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
        a.addFirst("a");
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addFirst("b");
        a.addLast("c");
        a.removeFirst();
        a.removeLast();


     }
 }
