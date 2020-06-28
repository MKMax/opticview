package io.github.mkmax.opticview.util;

public class Doubles {

    private static final double EPSILON = 1e-15d;

    public static boolean areEqual (double a, double b) {
        return Math.abs (a - b) <= EPSILON;
    }

}
