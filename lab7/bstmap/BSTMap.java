package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {

    private BSTNode root; // root of BST
    private int size;


    private class BSTNode {
        BSTNode left, right;
        private K key;
        private V value;

        public BSTNode(K k,V v) {
            this.key = k;
            this.value = v;
        }
    }

    /**
     * Initializes an empty symbol table
     */
    public BSTMap() {
        root = null;
        size = 0;
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private boolean containsKey_helper(K key, BSTNode r) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        if (r == null) { // if root became null means there is no value pair the key
            return false;
        }
        int compareValue = key.compareTo(r.key);
        // all situation
        if (compareValue < 0) {
            return containsKey_helper(key, r.left);
        } else if (compareValue > 0) {
            return containsKey_helper(key, r.right);
        } else {
            return true;
        }
    }




    @Override
    public boolean containsKey(K key) {
        return containsKey_helper(key, root);
    }


    /**
     * use recursion to finf the key value pair
     * @param key
     * @param r
     * @return
     */
    private V get_helper(K key, BSTNode r) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (r == null) { // if root became null means there is no value pair the key
            return null;
        }
        int compareValue = key.compareTo(r.key);
        // all situation
        if (compareValue < 0) {
            return get_helper(key, r.left);
        } else if (compareValue > 0) {
            return get_helper(key, r.right);
        } else {
            return r.value;
        }
    }

    @Override
    public V get(K key) {
        return get_helper(key, this.root);
    }



    @Override
    public int size() {
        return size;
    }

    /**
     * use recursive to help put function
      * @param key
     * @param value
     */
    private void put_helper(K key, V value, BSTNode r) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null to put()");
        }
        if (!containsKey(key)) {
            if (r == null) {
                return;
            }
            int compareValue = key.compareTo(r.key);
            if (compareValue < 0 && r.left != null) {
                put_helper(key, value, r.left);
            } else if (compareValue > 0 && r.right != null) {
                put_helper(key, value, r.right);
            } else if (compareValue > 0 && r.right == null) { // if left is null means
                r.right = new BSTNode(key, value);
            } else if (compareValue < 0 && r.left == null) {
                r.left = new BSTNode(key, value);
            }
        }
    }

    /**
     *  make all this value sorted BST
     */
    @Override
    public void put(K key, V value) {
        if (isEmpty()) {
            root = new BSTNode(key, value);
        } else {
            put_helper(key, value, this.root);
        }
        size ++;
    }

    @Override
    public Set<K> keySet() {
        Set<K> a = new HashSet<>();
        for (K x : this) {
            a.add(x);
        }
        return a;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        private Stack<BSTNode> stack;

        public BSTMapIterator() {
            stack = new Stack<>();
            pushAllLeft(root);
        }


        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            BSTNode returnBST =  stack.pop();
            pushAllLeft(returnBST.right);
            return returnBST.key;
        }

        private void pushAllLeft(BSTNode Node) {
            while (Node != null) {
               stack.push(Node);
               Node = Node.left;
            }
        }
    }

    public void printInOrder() {
        throw new UnsupportedOperationException();
    }
}
