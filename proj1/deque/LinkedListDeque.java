package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof LinkedListDeque oas) {
            if (oas.size() != this.size) {
                return false;
            }
            for (int i = 0; i < oas.size(); i++){
                if (oas.get(i) != this.get(i)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }



    private class LinkedListIterator implements Iterator<T> {

        TNode p;
        public LinkedListIterator() {
            this.p = sentinel;
        }
        @Override
        public boolean hasNext() {
            if (p.next.item == null) {
                return false;
            }
            return true;
        }

        @Override
        public T next() {
            T returnT = p.next.item;
            p = p.next;
            return returnT;
        }
    }

    //创建双链表的node
    private class TNode {
        //双向链表的实例对象
        private TNode prev;
        private T item;
        private TNode next;

        public TNode(TNode prev, T i, TNode next) {
            this.prev = prev;
            item = i;
            this.next = next;
        }

    }
    //随时跟踪item的数量
    private int size;
    private TNode sentinel;

    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel = new TNode(sentinel, null, sentinel);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    //添加到第一个
    public void addFirst(T x) {
        sentinel.next = new TNode(sentinel, x, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }


    //添加到最后一个
    public void addLast(T x) {
        sentinel.prev = new TNode(sentinel.prev, x, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }



    public T removeFirst() {
        TNode temp = sentinel.next;
        sentinel.next = temp.next;
        sentinel.next.prev = sentinel;
        if (size > 0) {
            size -= 1;
        }
        return temp.item;
    }

    public T removeLast() {
        TNode temp = sentinel.prev;
        sentinel.prev = temp.prev;
        sentinel.prev.next = sentinel;
        if (size > 0) {
            size -= 1;
        }
        return temp.item;
    }


    public T get(int i) {
        TNode temp = sentinel.next;
        if (i < size) {
            for (int num = 0; num < i; num++) {
                temp = temp.next;
            }
            return temp.item;
        } else {
            return null;
        }
    }

    private T getRecursivehelper(int index, TNode s) {
        /*
        get函数的递归方式
        */
        if (index == 0) {
            return s.item;
        } else {
            return getRecursivehelper(index - 1, s.next);
        }
    }


    public T getRecursive(int index) {
        return getRecursivehelper(index, sentinel.next);
    }
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        ArrayDeque<String> listofTs = new ArrayDeque<>();
        for (T x : this) {
            listofTs.addLast(x.toString());
        }
        return "{" + String.join(",", listofTs) + "}";
    }


    public void printDeque() {
        System.out.println(this);
    }




}
