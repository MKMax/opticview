package io.github.mkmax.util.math;

public class FloatingPoint {

    public static final float F32_EPSILON = 1e-7f;
    public static final double F64_EPSILON = 1e-16f;

    public static boolean equal (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON;
    }

    public static boolean equal (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON;
    }

}
