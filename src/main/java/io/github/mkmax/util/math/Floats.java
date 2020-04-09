package io.github.mkmax.util.math;

public class Floats {

    public static final float  F32_EPSILON_STRICT = 1e-6f;
    public static final float  F32_EPSILON_LEAN   = 1e-5f;

    public static final double F64_EPSILON_STRICT = 1e-15d;
    public static final double F64_EPSILON_LEAN   = 1e-14d;

    public static boolean strictEq (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON_STRICT;
    }

    public static boolean leanEq (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON_LEAN;
    }

    public static boolean strictEq (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON_STRICT;
    }

    public static boolean leanEq (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON_LEAN;
    }

    public static double clamp (double v, double l, double r) {
        return Math.max (Math.min (r, v), l);
    }
}
