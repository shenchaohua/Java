import java.beans.DesignMode;
import java.net.DatagramPacket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

class Node<K>{
    public K key;
    public Node<K> next;
    public Node<K> prev;
}

public class LRU<K,V>{

    private HashMap<K,V> storage;
    private Node head;
    private Integer cap;
    private Integer len;
    private Node tail;


    public LRU(HashMap<K, V> storage) {
        this.storage = storage;
    }

    public LRU(int cap) {
        this.cap = cap;
    }

    public static  <K,V> LRU newLRU(int cap){
        return new LRU<K,V>(cap);
    }

    private void MoveToHead(K key){
        removeNode(key);
        AddToHead(key);
    }

    public void removeNode(K key){

    }

    public void AddToHead(K key){

    }

    private void removeTail(){
        this.tail.prev.prev.next = this.tail;
    }

    public void set(K key,V value){
        this.storage.put(key,value);
        MoveToHead(key);
        len++;
        if(cap>len){
            removeTail();
            len--;
        }
    }

    public V get(K key){
        V v = this.storage.get(key);
        MoveToHead(key);
        return v;
    }

    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("s");
        list.add("ss");
        System.out.println(list);

        ArrayList<String> list1 = new ArrayList<>();
        list1.add("s");
        list1.remove(0);
    }
}
