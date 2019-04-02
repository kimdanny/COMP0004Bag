package uk.ac.ucl.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinkedListBag<T extends Comparable> extends AbstractBag<T> {

    private int maxSize;
    private Element<T> contents;

    private static class Element<E>
    {
        // act as a node
        public E value;
        public int occurrences;
        public Element<E> next;

        public Element(E value, int occurrences, Element<E> next)
        {
            this.value = value;
            this.occurrences = occurrences;
            this.next = next;
        }
    }

    public LinkedListBag() throws BagException
    {
        this(MAX_SIZE);
    }

    public LinkedListBag(int maxSize) throws BagException{
        if (maxSize > MAX_SIZE)
        {
            throw new BagException("Attempting to create a Bag with size greater than maximum");
        }
        if (maxSize < 1)
        {
            throw new BagException("Attempting to create a Bag with size less than 1");
        }

        this.maxSize = maxSize;
        contents = null;
    }

    @Override
    public void add(T value) throws BagException {

        if (size() < maxSize){
            // if value you try to put is not in the Element
            if (!contains(value)) {
                // and if head is null then put value and set occurrence to 1 for the first time
                if (contents == null) {
                    contents = new Element<>(value, 1, null);
                }
                // if it's not null traverse to the end of the Element and do the same thing
                else{
                    Element<T> nodeTraversing = contents;

                    while (nodeTraversing.next != null) {
                        nodeTraversing = nodeTraversing.next;
                    }

                    nodeTraversing.next = new Element<>(value, 1, null);
                }
            }
            // if value you try to put IS in the Element then addWhenContainsValue
            else{
                addWhenContainsValue(value);
            }
        }
        // you can still add value when maxSize only if the value you put is contained in the Element
        else if (size() == maxSize && contains(value)){
            addWhenContainsValue(value);
        }
        // Error handling part
        else {
            throw new BagException("Bag is full");
        }

    }

    // Helper method for add(T value)
    private void addWhenContainsValue(T value){
        Element<T> currentNode  = contents;

        // traversing and checking: if different, move next
        while(currentNode != null && !currentNode.value.equals(value)){
            currentNode = currentNode.next;
        }

        // if found, plus one to the according occurrence
        currentNode.occurrences = currentNode.occurrences + 1;
    }

    @Override
    public void addWithOccurrences(T value, int occurrences) throws BagException {

        for (int i = 0; i < occurrences; i++){
            add(value);
        }
    }

    @Override
    public boolean contains(T value) {
        for(T check : this){
            if(check.equals(value)){
                return true;
            }
        }
        return false;
    }

    // return the number of occurrences
    @Override
    public int countOf(T value) {
        Element<T> currentNode  = contents;

        // traversing and checking: if different, move next
        while(currentNode != null && !currentNode.value.equals(value)){
            currentNode = currentNode.next;
        }

        if(currentNode == null){
            throw new RuntimeException("No Nodes contain the value");
        }

        return currentNode.occurrences;
    }

    /*
    * remove one occurrence of the node
    * */
    @Override
    public void remove(T value) {
        Element<T> currentNode  = contents;
        Element<T> previousNode = null;

        // traversing and checking: if different, move next
        while(currentNode != null && !currentNode.value.equals(value)) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        if(currentNode == null){
            throw new RuntimeException("No Nodes contain the value");
        }

        if (currentNode.occurrences == 1){
            // remove the whole Node
            previousNode.next = currentNode.next;
        }
        else {
            // decrease one occurrence
            currentNode.occurrences = currentNode.occurrences - 1;
        }

    }

    /*
    * occurrences are not taken into account.
    * Therefore index for the number of nodes.
    * */
    @Override
    public int size() {
        int count = 0;
        Element<T> node = contents;

        while(node != null){
            node = node.next;
            count++;
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        if (contents == null){
            return true;
        }
        return false;
    }


    private class LinkedListUniqueIterator implements Iterator<T>{

        private Element<T> nextContents;

        public LinkedListUniqueIterator(){
            nextContents = contents;
        }

        @Override
        public boolean hasNext() {
            return nextContents != null;
        }

        @Override
        public T next() {

            if (!hasNext()){
                throw new NoSuchElementException("End of the element!");
            }
            T nextContentsValue = nextContents.value;
            nextContents = nextContents.next;
            return nextContentsValue;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListUniqueIterator();
    }

    private class LinkedListBagIterator implements Iterator<T>{

            private int index = 0;
            private int count = 1;

            Element<T> nodeTraversing = contents;

            @Override
            // see if is has next map or not
            public boolean hasNext() {

                // when there is some node after nodeTraversing
                if (nodeTraversing.next != null){
                    return true;
                }
                // when next node is null
                else {
                    while(nodeTraversing.occurrences > count){
                        ++count;
                        return true;
                    }
                    return false;
                }
            }

            @Override
            // returns next map's value
            public T next() {
                if (nodeTraversing.occurrences > index) {
                    index++;
                    return nodeTraversing.value;
                }
                else {
                    index = 1;
                    // Traverse
                    nodeTraversing = nodeTraversing.next;
                    return nodeTraversing.value;
                }
            }

    }

    @Override
    public Iterator<T> allOccurrencesIterator() {
        return new LinkedListBagIterator();
    }

}
