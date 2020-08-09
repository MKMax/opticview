package io.github.mkmax.opticview.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListUtilsTest {
    @Test
    public void pairCombinationIteratorTest () {
        final List<Integer> list = List.of (1, 2, 3);
        final List<Integer> result = new ArrayList<> (3);
        result.add (1);
        result.add (2);
        result.add (1);
        result.add (3);
        result.add (2);
        result.add (3);
        // 1 2 1 3 2 3
        final List<Integer> out = new ArrayList<> ();
        ListUtils.pairCombinationIterator (list, (a, b) -> {
            out.add (a);
            out.add (b);
        });
        assertEquals (result.size (), out.size ());
        for (int i = 0; i < result.size (); ++i)
            assertEquals (result.get (i), out.get (i));
    }
}
