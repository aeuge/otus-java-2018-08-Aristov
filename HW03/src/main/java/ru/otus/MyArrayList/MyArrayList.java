package ru.otus.MyArrayList;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private int size=0;
    transient Object[] myData;

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.myData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.myData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    public MyArrayList() {
        this.myData = EMPTY_ELEMENTDATA;
    }

    public MyArrayList(Collection<? extends T> c) {
        myData = c.toArray();
        if ((size = myData.length) != 0) {
            if (myData.getClass() != Object[].class)
                myData = Arrays.copyOf(myData, size, Object[].class);
        } else {
            this.myData = EMPTY_ELEMENTDATA;
        }
    }
    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return myData.length;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    private Object[] grow(int minCapacity) {
        return myData = Arrays.copyOf(myData,size+(size>>1)+minCapacity);
    }

    @Override
    public boolean add(T t) {
        if (size == myData.length)
            myData = grow(1);
        myData[size] = t;
        size += 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) myData, 0, size, c);
    }

    @Override
    public void clear() {
        //
    }

    T myData(int index) {
        return (T) myData[index];
    }
    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return myData(index);
    }

    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        T oldValue = myData(index);
        myData[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, T element) {
        //
    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException("error");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }
    protected transient int modCount = 0;

    private class ListItr  implements ListIterator<T> {
        int cursor = 0;

        /**
             * Index of element returned by most recent call to next or
             * previous.  Reset to -1 if this element is deleted by a call
             * to remove.
             */
        int lastRet = -1;

        ListItr(int index) {
            cursor = index;
        }
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = myData;
            if ( i >= myData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (T) myData[(lastRet = i)];
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public T previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(T e) {

        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (size>0) {
            String s = "[";
            for (int i = 0; i < size - 1; i++) {
                s += myData[i] + ", ";
            }
            s += myData[size-1] + "]";
            return s;
        } else {
            return "";
        }
    }
}
