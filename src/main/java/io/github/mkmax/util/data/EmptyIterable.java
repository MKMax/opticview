package io.github.mkmax.util.data;

import java.util.Collections;
import java.util.Iterator;

public class EmptyIterable<T> implements Iterable<T> {

    public static final EmptyIterable<?> INSTANCE = new EmptyIterable<> ();

    @SuppressWarnings ("unchecked")
    public static <T> EmptyIterable<T> adaptedInstance () {
        return (EmptyIterable<T>) INSTANCE;
    }

    private EmptyIterable () { }

    @Override
    public Iterator<T> iterator () {
        return Collections.emptyIterator ();
    }

}
