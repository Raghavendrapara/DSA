import java.util.*;

class LRUCache {

    HashMap<Integer,Integer> map;
    HashMap<Integer,Integer> freqMap;
    PriorityQueue<Node> frequency;
    int capacity;

    public LRUCache(int capacity) {
        map=new HashMap<>(capacity);
        frequency=new PriorityQueue<>(capacity,(a,b)->a.frequency-b.frequency);
        this.capacity=capacity;
    }
    
    public int get(int key) {
        freqMap.put(key,freqMap.getOrDefault(key,0)+1);
        return map.getOrDefault(key,-1);
    }
    
    public void put(int key, int value) {
        if(map.size()==capacity && !map.containsKey(key)){
            //delete LRU

        }
        map.put(key,value);
    }
    class Node{
        int key ;
        int frequency;
    }
}
