package com.redis_bookmyshow.show.algorithms;

import java.util.Map;

public class LRUCache<K, V> {
    private int capacity;
    private Map<K, Node<K, V>> map;

    private Node<K, V> head;
    private Node<K, V> tail;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.map = new java.util.HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(2);

        cache.put(1, "A"); // [1]
        cache.put(2, "B"); // [2,1]
        System.out.println(cache.get(1)); // A  -> [1,2]
        cache.put(3, "C"); // evicts 2 -> [3,1]
        System.out.println(cache.get(2)); // null
        cache.put(4, "D"); // evicts 1 -> [4,3]
        System.out.println(cache.get(1)); // null
        System.out.println(cache.get(3)); // C
        System.out.println(cache.get(4)); // D
    }

    public V get(K key){
        Node<K, V> node = map.get(key);
        if (node == null) return null;
        moveToFront(node);
        return node.value;
    }

    public void moveToFront(Node<K, V> node){
        removeNode(node);
        addToFront(node);
    }

    public void removeNode(Node<K, V> node){
        Node<K, V> left = node.prev;
        Node<K, V> right = node.next;
        left.next = right;
        right.prev = left;

        node.prev = null;
        node.next = null;
    }

    public void addToFront(Node<K, V> node){
        Node<K, V> first = head.next;

        node.prev = head;
        node.next = first;

        head.next = node;
        first.prev = node;
    }

    public void put(K key, V value){
        Node<K, V> node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToFront(node);
            return;
        }

        Node<K, V> newNode = new Node<>(key, value);
        addToFront(newNode);
        map.put(key, newNode);
        if (map.size() > capacity){
            Node<K, V> lru = removeLRU();
            if (lru != null) {
                map.remove(lru.key);
            }
        }
    }

    public  Node<K, V> removeLRU(){
        Node<K, V> lru = tail.prev;
        if (lru == head)
                return null;
        removeNode(lru);
        return lru;
    }

}

class Node<K, V>{
    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
