package pt.gapiap.convert;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListCv<O, C> implements List<C> {
    private List<O> original;
    private Conversor<O,C> conversor;

    protected ListCv(List<O> original, Conversor<O, C> conversor) {
        this.original = original;
        this.conversor = conversor;
    }

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public boolean isEmpty() {
        return original.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return original.contains(o);
    }

    @Override
    public Iterator<C> iterator() {
        return new CvIterator<>(original.iterator(),conversor);
    }

    @Override
    public Object[] toArray() {
        return original.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return original.toArray(a);
    }

    public boolean add(C o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        return original.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return original.containsAll(c);
    }

    public boolean addAll(Collection<? extends C> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends C> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C get(int index) {
        conversor.setOriginal(original.get(index));
        return conversor.getConverted();
    }

    @Override
    public C set(int index, C element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, C element) {

    }

    @Override
    public C remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        return original.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return original.lastIndexOf(o);
    }

    @Override
    public ListIterator<C> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<C> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<C> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }


    private static class CvIterator<C, O> implements Iterator<C> {
        private final Conversor<O, C> conversor;
        Iterator<O> iterator;

        private CvIterator(Iterator<O> iterator,Conversor<O,C> conversor) {
            this.iterator = iterator;
            this.conversor = conversor;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public C next() {
            conversor.setOriginal(iterator.next());
            return conversor.getConverted();
        }


        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
