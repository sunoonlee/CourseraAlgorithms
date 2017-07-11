package assg2;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Programming Assignment 2: Deques and Randomized Queues
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 * Deque: A double-ended queue
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        // construct an empty deque
    }

    public boolean isEmpty() {
        return N == 0;
    }
    public int size() {
        return N;
    }
    public void addFirst(Item item) {
        // add the item to the front
        if (item == null) throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) last = first;
        else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        N++;
    }
    public void addLast(Item item) {
        // add the item to the end
        if (item == null) throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else {
            oldlast.next = last;
            last.prev = oldlast;
        }
        N++;
    }
    public Item removeFirst() {
        // remove and return the item from the front
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        N--;
        return item;
    }
    public Item removeLast() {
        // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        N--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {throw new UnsupportedOperationException();}
        public Item next() {
            if (current == null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        // unit testing
        Deque<String> dq = new Deque<String>();

        dq.addFirst("b");
        dq.addFirst("a");
        dq.addLast("c");
        dq.addLast("d");
        assert dq.size() == 4;

        try { dq.addLast(null); }
        catch (IllegalArgumentException e) { System.out.println("exception handled: add null"); }

        dq.removeFirst();
        dq.removeLast();
        String dq_tester = "";
        for (String s: dq) { dq_tester += s;}
        assert dq_tester == "bc";

        dq.removeLast();
        dq.removeFirst();
        assert dq.isEmpty() == true;

        try { dq.removeFirst(); }
        catch (NoSuchElementException e) { System.out.println("exception handled: remove from empty"); }

        dq.addLast("x");
        Iterator<String> it = dq.iterator();
        assert it.hasNext() == true;
        String ne = it.next();
        assert ne == "x";  // 若直接用 `assert it.next() == "x";`, it.next() 并不会真正执行?
        assert it.hasNext() == false;

        try { it.next(); }
        catch (NoSuchElementException e) { System.out.println("exception handled: no next"); }

        try { it.remove(); }
        catch (UnsupportedOperationException e) { System.out.println("exception handled: no remove"); }

        System.out.println("test done.");
    }
}
