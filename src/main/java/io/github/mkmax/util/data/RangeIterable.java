package io.github.mkmax.util.data;

import java.util.Iterator;
import java.util.Objects;

public final class RangeIterable<T> implements Iterable<T> {

    private final Iterable<T> source;
    private       int         begin;
    private       int         end;

    public RangeIterable (
        Iterable<T> pSource,
        int         pBegin,
        int         pEnd)
    {
        source = Objects.requireNonNull (pSource);
        withRange (pBegin, pEnd);
    }

    public RangeIterable (Iterable<T> pSource) {
        this (pSource, 0, Integer.MAX_VALUE);
    }

    public RangeIterable<T> withRange (int pBegin, int pEnd) {
        if (pBegin < 0)
            throw new IllegalArgumentException ("Starting index out of bounds by < 0");
        if (pEnd < pBegin)
            throw new IllegalArgumentException ("Starting index is ahead of end");
        begin = pBegin;
        end   = pEnd;
        return this;
    }

    @Override
    public Iterator<T> iterator () {
        return new RangeIterator<> (source.iterator (), begin, end);
    }
}