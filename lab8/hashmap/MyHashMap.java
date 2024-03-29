package hashmap;


import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private void resize(int chains) {
        MyHashMap<K, V> newHashmap = new MyHashMap<K, V>(chains);
        for (int i = 0; i < tableSize; i++) {
            if (buckets[i] == null) {
                continue;
            }
            for (Node node : buckets[i]) {
               newHashmap.put(node.key, node.value);
            }
        }
        this.buckets = newHashmap.buckets;
        this.size = newHashmap.size;
        this.maxLoad = newHashmap.maxLoad;
        this.tableSize = newHashmap.tableSize;
    }
    @Override
    public void clear() {
        MyHashMap<K, V> newHashmap = new MyHashMap<K, V>();
        this.buckets = newHashmap.buckets;
        this.size = newHashmap.size;
        this.maxLoad = newHashmap.maxLoad;
        this.tableSize = newHashmap.tableSize;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null in containsKey() function");
        }
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("the key is null in get() function");
        }
        Collection<Node> bucket = getBucket(key);
        if (!(bucket == null)) {
            for (Node x : bucket) {
                if (x.key.equals(key)) {
                    return x.value;
                }
            }
        }
        return null;
    }

    // hash function for keys - return value between 0 and tableSize - 1
    private int hashTextbook(K key) {
        return (key.hashCode() & 0x7fffffff) % tableSize;
    }
    /**
     * get the Bucket by key
     * @param key
     * @return
     */
    private Collection<Node> getBucket(K key) {
        int keyHashcode = hashTextbook(key) ;
        if (keyHashcode < 0) {
           keyHashcode = keyHashcode + tableSize;
        }
        return buckets[keyHashcode];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Collection<Node> bucket = getBucket(key);
        // if the bucket is null
        if (bucket == null) {
            int position = hashTextbook(key);
            buckets[position] = createBucket();
            bucket = buckets[position];
        }

        if (containsKey(key)) { // if this map already exist
            for (Node x : bucket) { // iterate the bucket
                if (x.key.equals(key)) { // find the exact key
                    x.value = value;
                }
            }
        } else {
            // if there is no that map add one to this bucket
            bucket.add(createNode(key, value));
            size++;
            double load = size / tableSize;
            // if items too much use resize to make hashtable bigger
            if (load > maxLoad) {
                resize(size * 2);
            }
        }
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keySetResult = new HashSet<>();
        for (int i = 0; i < tableSize; i++) {
            if (buckets[i] == null) {
                continue;
            }
            for (Node x : buckets[i]) {
                keySetResult.add(x.key);
            }
        }
        return keySetResult;
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
        throw new UnsupportedOperationException();
    }

    private class MyHashMapIterator<K> implements Iterator<K> {
        int bucketNumber;

        public MyHashMapIterator() {
            bucketNumber = 0;
        }
        @Override
        public boolean hasNext() {
            return bucketNumber < tableSize;
        }

        @Override
        public K next() {
            Collection<Node> nowBucket = buckets[bucketNumber];
            Iterator nowBucketIterator = nowBucket.iterator();
            if (!nowBucketIterator.hasNext()) {
                 Node resultNode = (Node)nowBucketIterator.next();
                 return (K)resultNode.key;
            } else {
                bucketNumber += 1;
                return next();
            }
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private static final int INIT_CAPACITY = 4;
    private double maxLoad;

    private int size;
    private int tableSize;

    /** Constructors */
    public MyHashMap() {
        this(INIT_CAPACITY, 1.5);
    }

    // constructor with one parameter
    public MyHashMap(int initialSize) {
        this(initialSize, 1.5);
    }


    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        tableSize = initialSize;
        buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        size = 0;
    }


    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        Node newNode = new Node(key, value);
        return newNode;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] Table =  new Collection[tableSize];
        return Table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
