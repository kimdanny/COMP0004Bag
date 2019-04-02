package uk.ac.ucl.bag;

import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MapBag<T extends Comparable> extends AbstractBag<T> {

    private int maxSize;
    private HashMap<T, Integer> contents;

    public MapBag() throws BagException
    {
        this(MAX_SIZE);
    }

    public MapBag(int maxSize) throws BagException{
        if (maxSize > MAX_SIZE)
        {
            throw new BagException("Attempting to create a Bag with size greater than maximum");
        }
        if (maxSize < 1)
        {
            throw new BagException("Attempting to create a Bag with size less than 1");
        }

        this.maxSize = maxSize;
        this.contents = new HashMap<>();
    }

    @Override
    public void add(T key) throws BagException{
        if (contents.size() < maxSize) {

            // if key is contained, then increase the occurrence by 1
            if (contents.containsKey(key)){
                contents.put(key, contents.get(key) + 1);

            }
            else{
                contents.put(key, 1);
            }

        }
        // Error handling
        else{
            throw new BagException("Bag is full");
        }

    }

    @Override
    public void addWithOccurrences(T key, int occurrences) throws BagException{

        for (int i = 0; i < occurrences; i++){
            add(key);
        }

    }

    @Override
    public boolean contains(T key){
        if (contents.containsKey(key)){
            return true;
        }
        return false;
    }

    @Override
    // simply returns the occurrence
    public int countOf(T key){
        if (contains(key)){
            return contents.get(key);
        }
        else{
            return 0;
        }
    }

    @Override
    public void remove(T key){
        // if key is contained in key
        if (contents.containsKey(key)){
            // and if the occurrences are greater or equal to 2, then decrease it by 1
            if (contents.get(key) >= 2){
                contents.put(key, contents.get(key) - 1);
            }
            // if the occurrence is just 1 then remove whole key and value
            // in order not to have key with 0 occurrence
            else if (contents.get(key) == 1){
                contents.remove(key);
            }
        }

    }

    @Override
    public boolean isEmpty(){
        if (contents.isEmpty()){
            return true;
        }else return false;
    }

    @Override
    public int size(){
        return contents.size();
    }


    private class MapBagUniqueIterator implements Iterator<T>
    {
        private Iterator<T> iterator = contents.keySet().iterator();

        @Override
        public boolean hasNext(){
            return iterator.hasNext();
        }

        @Override
        public T next(){
            if (!hasNext()){
                throw new NoSuchElementException("End of the element!");
            }
            return iterator.next();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MapBagUniqueIterator();
    }


    private class MapBagIterator implements Iterator<T>
    {
        private int index = 0;
        private int count = 0;

        private ArrayList<T> keyList = new ArrayList<>(contents.keySet());
        private ArrayList<Integer> valueList = new ArrayList<>(contents.values());

        @Override
        // see if is has next map or not
        public boolean hasNext(){
            if (index < size()){
                if(count < valueList.get(index)){
                    return true;
                }
            }
            return false;
        }

        @Override
        // returns next map's value
        public T next(){

            if (count < valueList.get(index)){
                T value = keyList.get(index);
                count++;

                if (count == valueList.get(index)){
                    count = 0;
                    index++;
                }
                return value;
            }else{
                throw new IllegalStateException();
            }

        }
    }

    @Override
    public Iterator<T> allOccurrencesIterator() {
        return new MapBagIterator();
    }
}
