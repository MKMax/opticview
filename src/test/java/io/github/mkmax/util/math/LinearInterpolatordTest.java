package io.github.mkmax.util.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinearInterpolatordTest {

    @Test
    void testMap () {
        LinearInterpolatord interp = new LinearInterpolatord (
            -1d, 0d,
             1d, 128d
        );

        assertEquals (  0d, interp.map (-1d));
        assertEquals ( 64d, interp.map ( 0d));
        assertEquals (128d, interp.map ( 1d));

        assertEquals (-1d, interp.unmap (  0d));
        assertEquals ( 0d, interp.unmap ( 64d));
        assertEquals ( 1d, interp.unmap (128d));
    }

}
