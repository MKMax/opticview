package me.maximkasyanenko.mathfx.util;

public class Floats {

    private static final float F32_EPSILON = 1e-7f;
    private static final double F64_EPSILON = 1e-16f;

    public static boolean equal (float f32a, float f32b) {
        return Math.abs (f32a - f32b) <= F32_EPSILON;
    }

    public static boolean equal (double f64a, double f64b) {
        return Math.abs (f64a - f64a) <= F64_EPSILON;
    }

}
