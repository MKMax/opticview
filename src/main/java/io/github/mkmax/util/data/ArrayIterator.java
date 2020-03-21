package io.github.mkmax.util.data;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

    public static <T> ArrayIterator<T> empty () {
        return new ArrayIterator<> (null);
    }

    private final T[] elements;
    private       int index;
    private       int limit;

    public ArrayIterator (T[] elements, int begin, int end) {
        this.elements = elements;
        this.index    = Math.max (0, begin);
        if (elements != null)
            this.limit = Math.min (elements.length, end);
    }

    public ArrayIterator (T[] elements, int end) {
        this (elements, 0, end);
    }

    public ArrayIterator (T[] elements) {
        this (elements, 0, elements == null ? 0 : elements.length);
    }

    @Override
    public boolean hasNext () {
        return elements != null && index < limit;
    }

    @Override
    public T next () {
        return elements[index++];
    }
}
