package io.github.mkmax.opticview.util;

import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;

public class ListUtils {

    public static <T> void pairCombinationIterator (
        final List<? extends T> list, BiConsumer<? super T, ? super T> action)
    {
        if (list == null || action == null)
            return;
        ListIterator<? extends T> it = list.listIterator ();
        while (it.hasNext ()) {
            final int i = it.nextIndex ();
            final T obj = it.next ();
            if (i < list.size ()) {
                ListIterator<? extends T> subIt = list.listIterator (i + 1);
                while (subIt.hasNext ())
                    action.accept (obj, subIt.next ());
            }
        }
    }

}
