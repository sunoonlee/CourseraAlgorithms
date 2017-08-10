package assg2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Programming Assignment 2: Deques and Randomized Queues
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 * RandomizedQueue: A queue of which the item removed is chosen uniformly at random
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1];
    private int N = 0;
    public RandomizedQueue() {}

    public boolean isEmpty() { return N == 0; }
    public int size() { return N; }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) { temp[i] = a[i]; }
        a = temp;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (N == a.length) resize(2 * a.length);
        a[N++] = item;
    }

    public Item dequeue() {
        // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException();
        int rand = StdRandom.uniform(N);
        Item item = a[rand];
        a[rand] = a[N-1];
        a[N-1] = null;
        N--;
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
    }

    public Item sample() {
        // return (but do not remove) a random item
        if (isEmpty()) throw new NoSuchElementException();
        int rand = StdRandom.uniform(N);
        Item item = a[rand];
        return item;
    }

    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {
        // support random iteration
        private int ids[] = StdRandom.permutation(N);
        private int i = 0;
        public boolean hasNext() { return i < N; }
        public Item next() {
            if (i == N) throw new NoSuchElementException();
            return a[ids[i++]];
        }
        public void remove() { throw new UnsupportedOperationException();}
    }

    public static void main(String[] args) {
        // unit testing
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        rq.enqueue("e");
        assert rq.size() == 5;

        System.out.println("** 3 samples:");
        for (int i=0; i<3; i++) { System.out.println(rq.sample()); }

        System.out.println("** dequeue twice:");
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        assert rq.size() == 3;

        for (int i=0; i<3; i++) {
            for (String s: rq) { System.out.print(s); }
            System.out.println("");
        }

        for (int i=0; i<3; i++) { rq.dequeue(); }
        assert rq.isEmpty() == true;
        rq.dequeue();
    }
}

