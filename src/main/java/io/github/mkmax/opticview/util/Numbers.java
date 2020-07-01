package io.github.mkmax.opticview.util;

public class Numbers {

    /* +------------------+ */
    /* | DOUBLE UTILITIES | */
    /* +------------------+ */
    private static final double DOUBLE_EPSILON = 1e-15d;

    public static boolean areEqual (double a, double b) {
        return Math.abs (a - b) <= DOUBLE_EPSILON;
    }

    public static double clamp (double val, double bottom, double top) {
        if (bottom > top)
            return clamp (val, top, bottom);
        return Math.max (Math.min (val, top), bottom);
    }

    /* +---------------+ */
    /* | INT UTILITIES | */
    /* +---------------+ */
    public static int clamp (int val, int bottom, int top) {
        if (bottom > top)
            return clamp (val, top, bottom);
        return Math.max (Math.min (val, top), bottom);
    }
}
