package io.github.mkmax.util.data;

import java.util.Iterator;

public class ArrayIterable<T> implements Iterable<T> {

    public static <T> ArrayIterable<T> empty () {
        return new ArrayIterable<> (null);
    }

    private final T[] elements;
    private       int begin;
    private       int end;

    public ArrayIterable (T[] elements, int begin, int end) {
        this.elements = elements;
        this.begin    = Math.max (0, begin);
        if (elements != null)
            this.end = Math.min (elements.length, end);
    }

    public ArrayIterable (T[] elements, int end) {
        this (elements, 0, end);
    }

    public ArrayIterable (T[] elements) {
        this (elements, 0, elements == null ? 0 : elements.length);
    }

    @Override
    public Iterator<T> iterator () {
        return new ArrayIterator<> (elements, begin, end);
    }
}
