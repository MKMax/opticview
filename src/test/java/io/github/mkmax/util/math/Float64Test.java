package io.github.mkmax.util.math;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Float64Test {

    @Test
    void testExcludeSelf () {
        List<Double> self = new ArrayList<> ();
        List<Double> excl = new ArrayList<> ();

        Collections.addAll (self, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        Collections.addAll (excl, 2.0, 4.0, 6.0);

        Float64.excludeSelf (self, excl);

        assertEquals (4, self.size ());
        assertTrue (Float64.strictEq (1.0, self.get (0)));
        assertTrue (Float64.strictEq (3.0, self.get (1)));
        assertTrue (Float64.strictEq (5.0, self.get (2)));
        assertTrue (Float64.strictEq (7.0, self.get (3)));
    }
}
