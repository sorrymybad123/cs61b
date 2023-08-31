package deque;

import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<Item> implements Iterable<Item>, Deque<Item> {
    @Override
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }


    private class LinkedListIterator implements Iterator<Item> {

        ItemNode p;
        public LinkedListIterator(){
            this.p = sentinel;
        }
        @Override
        public boolean hasNext() {
            if (p.next.item == null){
                return false;
            }
            return true;
        }

        @Override
        public Item next() {
            Item returnItem = p.next.item;
            p = p.next;
            return returnItem;
        }
    }

    //创建双链表的node
     private class ItemNode{
         //双向链表的实例对象
         public ItemNode prev;
         public Item item;
         public ItemNode next;

         public ItemNode(ItemNode prev, Item i, ItemNode next){
             this.prev = prev;
             item = i;
             this.next = next;
         }

    }
    //随时跟踪item的数量
    private int size;
     private ItemNode sentinel;

     public LinkedListDeque(){
         sentinel = new ItemNode(null, null, null);
         sentinel = new ItemNode(sentinel, null, sentinel);
         sentinel.next = sentinel;
         sentinel.prev = sentinel;
         size = 0;
     }
     public LinkedListDeque(Item x){
         sentinel = new ItemNode(null, null, null);
         sentinel = new ItemNode(sentinel, null, sentinel);
         sentinel.next = sentinel;
         sentinel.prev = sentinel;
         sentinel.next = new ItemNode(sentinel, x, sentinel.prev);
         size = 1;
     }

     //添加到第一个
     public void addFirst(Item x){
        sentinel.next = new ItemNode(sentinel, x, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
     }


     //添加到最后一个
     public void addLast(Item x){
         sentinel.prev = new ItemNode(sentinel.prev, x, sentinel);
         sentinel.prev.prev.next = sentinel.prev;
         size += 1;
     }



    public Item removeFirst(){
             ItemNode temp = sentinel.next;
             sentinel.next = temp.next;
             sentinel.next.prev = sentinel;
         if (size > 0){
             size -= 1;
         }
             return temp.item;

     }

     public Item removeLast(){
             ItemNode temp = sentinel.prev;
             sentinel.prev = temp.prev;
             sentinel.prev.next = sentinel;
             if (size > 0){
                 size -= 1;
             }
             return temp.item;
     }


     public Item get(int i){
         ItemNode temp = sentinel.next;
         if (i < size){
             for (int num = 0; num < i; num++){
                temp = temp.next;
             }
             return temp.item;
         }else{
             return null;
         }
     }

    private Item getRecursivehelper(int index, ItemNode sentinel){
         /*
         get函数的递归方式
          */
         if (index == 0){
             return sentinel.item;
         }else{
             return getRecursivehelper(index - 1, sentinel.next);
         }
    }


     public Item getRecursive(int index){
         return getRecursivehelper(index, sentinel.next);
     }
     public int size(){
         return size;
     }

    @Override
    public String toString(){
        ArrayDeque<String> listofItems = new ArrayDeque<>();
        for (Item x : this){
            listofItems.addLast(x.toString());
        }
        return "{" + String.join(",", listofItems) + "}";
    }


     public void printDeque(){
          System.out.println(this);
     }

    public static void main(String[] args) {
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();
        ld.addLast(1);
        ld.addLast(2);
        ld.addLast(3);
        ld.addLast(null);
        ld.addFirst(4);
        ld.printDeque();
    }


}
