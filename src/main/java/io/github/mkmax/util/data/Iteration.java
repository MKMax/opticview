package io.github.mkmax.util.data;

import java.util.ArrayList;
import java.util.Iterator;

public class Iteration {

    public static <T> ArrayList<T> toArrayList (Iterator<T> iterator) {
        final ArrayList<T> result = new ArrayList<> ();
        while (iterator.hasNext ())
            result.add (iterator.next ());
        return result;
    }

    public static <T> ArrayList<T> toArrayList (Iterable<T> iterable) {
        return toArrayList (iterable.iterator ());
    }
}
