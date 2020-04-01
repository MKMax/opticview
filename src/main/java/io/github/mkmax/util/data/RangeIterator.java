package io.github.mkmax.util.data;

import java.util.Iterator;
import java.util.Objects;

public final class RangeIterator<T> implements Iterator<T> {

    private final Iterator<T> source;
    private final int         end;
    private       int         index;

    public RangeIterator (
        Iterator<T> pSource,
        int         pBegin,
        int         pEnd)
    {
        source = Objects.requireNonNull (pSource);

        if (pBegin < 0)
            throw new IllegalArgumentException ("Starting index out of bounds by < 0");
        if (pEnd < pBegin)
            throw new IllegalArgumentException ("Starting index is ahead of end");

        index  = pBegin;
        end    = pEnd;

        /* loop through the source iterator to the beginning index */
        int i = 0;
        while (i < pBegin && source.hasNext ()) {
            source.next ();
            ++i;
        }
    }

    @Override
    public boolean hasNext () {
        return index < end && source.hasNext ();
    }

    @Override
    public T next () {
        ++index;
        return source.next ();
    }
}