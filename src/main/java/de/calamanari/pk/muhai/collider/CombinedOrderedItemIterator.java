package de.calamanari.pk.muhai.collider;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * The {@link CombinedOrderedItemIterator} iterates over all the given input iterators and creates an ordered squence. Therefore we assume that the input
 * iterators return the elements of their sources sorted according to the natural order of the elements. <br />
 * If so, this iterator returns the total sequence of elements in their natural order.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <E> elements taken from the sources and returned by this iterator
 */
public class CombinedOrderedItemIterator<E extends Comparable<E>> implements Iterator<E> {

    /**
     * A given number of sources we want to read while ordering the inputs
     */
    private final Collection<Iterator<E>> sourceIterators;

    /**
     * Tracks the one element per input source and implicitly orders the elements
     */
    private final PriorityQueue<CandidateWithOrigin> candidates;

    /**
     * Indicates that there are no more elements available
     */
    private boolean done;

    /**
     * Creates a combined iterator
     * @param sourceIterators assumed to return ordered elements
     */
    public CombinedOrderedItemIterator(Collection<Iterator<E>> sourceIterators) {
        this.sourceIterators = sourceIterators;
        this.candidates = new PriorityQueue<>(Math.max(1, this.sourceIterators.size()));
    }

    @Override
    public boolean hasNext() {
        boolean res = !candidates.isEmpty();
        if (!res && !done) {
            if (candidates.isEmpty()) {
                // initialize
                for (Iterator<E> it : sourceIterators) {
                    if (it.hasNext()) {
                        candidates.add(new CandidateWithOrigin(it.next(), it));
                    }
                }
            }
            if (candidates.isEmpty()) {
                done = true;
            }
            else {
                res = true;
            }
        }
        return res;
    }

    @Override
    public E next() {
        E res = null;
        if (this.hasNext()) {
            // take the head, which is the smallest element (natural order according to the elements compareTo)
            CandidateWithOrigin cwo = candidates.poll();
            res = cwo.getItem();

            // refill queue from same origin if possible
            Iterator<E> origin = cwo.getOrigin();
            if (origin.hasNext()) {
                candidates.add(new CandidateWithOrigin(origin.next(), origin));
            }
        }
        else {
            throw new NoSuchElementException("End Of Input");
        }
        return res;
    }

    /**
     * For taking the next element from the same source iterator (next()), we need to track the origin, this class keeps the tuple. This is a utility object
     * that ignores {@link #origin} when computing equality, hashcode and comparing elements. We can do that here because elements never leave the context of
     * this class.
     */
    private class CandidateWithOrigin implements Comparable<CandidateWithOrigin> {

        private final E item;

        private final Iterator<E> origin;

        CandidateWithOrigin(E item, Iterator<E> origin) {
            this.item = item;
            this.origin = origin;
        }

        public E getItem() {
            return item;
        }

        public Iterator<E> getOrigin() {
            return origin;
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            boolean res = false;
            if (obj != null && this.getClass() == obj.getClass()) {
                @SuppressWarnings("unchecked")
                CandidateWithOrigin other = (CandidateWithOrigin) obj;
                res = this.item.equals(other.item);
            }
            return res;
        }

        @Override
        public int compareTo(CandidateWithOrigin o) {
            return this.item.compareTo(o.item);
        }

    }

}
