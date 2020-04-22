package io.github.mkmax.util.math;

public class Float32 {

    public static final float EPSILON_STRICT  = 1e-6f;
    public static final float EPSILON_LEAN    = 1e-5f;

    public static boolean equal (float a, float b, float epsilon) {
        return Math.abs (a - b) <= epsilon;
    }

    public static boolean strictEq (float a, float b) {
        return equal (a, b, EPSILON_STRICT);
    }

    public static boolean leanEq (float a, float b) {
        return equal (a, b, EPSILON_LEAN);
    }

    public static float clamp (float v, float l, float r) {
        return Math.max (Math.min (r, v), l);
    }
}