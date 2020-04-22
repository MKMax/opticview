package io.github.mkmax.util.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Int32Test {

    @Test
    void testClog () {
        assertEquals (0x80_00_00_00, Int32.clog (0));
        assertEquals (0, Int32.clog (1));
        assertEquals (1, Int32.clog (2));
        assertEquals (6, Int32.clog (63));
        assertEquals (6, Int32.clog (64));
        assertEquals (16, Int32.clog (65535));
        assertEquals (16, Int32.clog (65536));
        assertEquals (31, Int32.clog (0x80_00_00_00));
        assertEquals (32, Int32.clog (0xff_ff_ff_ff));
    }

    @Test
    void testFlog () {
        assertEquals (0x80_00_00_00, Int32.flog (0));
        assertEquals (0, Int32.flog (1));
        assertEquals (1, Int32.flog (2));
        assertEquals (5, Int32.flog (63));
        assertEquals (6, Int32.flog (64));
        assertEquals (15, Int32.flog (65535));
        assertEquals (16, Int32.flog (65536));
        assertEquals (31, Int32.flog (0x80_00_00_00));
        assertEquals (31, Int32.flog (0xff_ff_ff_ff));
    }
}
