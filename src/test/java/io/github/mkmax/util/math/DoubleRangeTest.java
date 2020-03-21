package io.github.mkmax.util.math;

import io.github.mkmax.util.math.DoubleRange.Lerp;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class DoubleRangeTest {

    @Test
    void testLerpExpectedCase_0 () {
        final DoubleRange from = new DoubleRange (-1, +1);
        final DoubleRange to   = new DoubleRange (0, 1280);
        final Lerp        lerp = new Lerp (from, to);

        assertTrue (FloatingPoint.strictEq (0d,    lerp.project (-1.0d)));
        assertTrue (FloatingPoint.strictEq (320d,  lerp.project (-0.5d)));
        assertTrue (FloatingPoint.strictEq (640d,  lerp.project ( 0.0d)));
        assertTrue (FloatingPoint.strictEq (960d,  lerp.project ( 0.5d)));
        assertTrue (FloatingPoint.strictEq (1280d, lerp.project ( 1.0d)));
    }

    @Test
    void testLerpExpectedCase_1 () {
        final DoubleRange from = new DoubleRange (0, 0);
        final DoubleRange to   = new DoubleRange (0, 128);
        final Lerp        lerp = new Lerp (from, to);

        assertTrue (Double.isNaN (lerp.project (-1.0d)));
        assertTrue (Double.isNaN (lerp.project ( 0.0d)));
        assertTrue (Double.isNaN (lerp.project ( 1.0d)));
    }
}
