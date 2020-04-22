package io.github.mkmax.util.math;

public class Float64 {

    public static final double EPSILON_STRICT  = 1e-15d;
    public static final double EPSILON_LEAN    = 1e-14d;

    public static boolean equal (double a, double b, double epsilon) {
        return Math.abs (a - b) <= epsilon;
    }

    public static boolean strictEq (double a, double b) {
        return equal (a, b, EPSILON_STRICT);
    }

    public static boolean leanEq (double a, double b) {
        return equal (a, b, EPSILON_LEAN);
    }

    public static double clamp (double v, double l, double r) {
        return Math.max (Math.min (r, v), l);
    }

}
