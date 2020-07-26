package io.github.mkmax.opticview.util;

public class IntegerUtils {

    /* +----------+ */
    /* | CLAMPING | */
    /* +----------+ */
    public static int clamp (int val, int bottom, int top) {
        if (bottom > top)
            return clamp (val, top, bottom);
        return Math.max (Math.min (val, top), bottom);
    }

    /* +------------+ */
    /* | LOGARITHMS | */
    /* +------------+ */
    public static int floorLog2 (int val) {
        if (val == 0)
            return -1;
        int res = 31, i = 1 << 31;
        while ((val & i) == 0) {
            i >>= 1;
            --res;
        }
        return res;
    }
}
