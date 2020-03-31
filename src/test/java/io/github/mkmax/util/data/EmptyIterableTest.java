package io.github.mkmax.util.data;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class EmptyIterableTest {

    @Test
    void testAdaptedInstance_0 () {
        Iterable<String> iterable = EmptyIterable.adaptedInstance ();
        Iterator<String> iterator = iterable.iterator ();

        assertFalse (iterator.hasNext ());
    }
}
