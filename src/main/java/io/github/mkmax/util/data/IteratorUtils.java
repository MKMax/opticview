package io.github.mkmax.util.data;

import java.util.ArrayList;
import java.util.Objects;

public class IteratorUtils {

    public static <T> ArrayList<T> newArrayList (Iterable<T> items) {
        Objects.requireNonNull (items);
        final ArrayList<T> result = new ArrayList<> ();
        items.forEach (result::add);
        return result;
    }

}
