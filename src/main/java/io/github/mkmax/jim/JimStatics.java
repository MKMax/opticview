package io.github.mkmax.jim;

/**
 * A collection of miscellaneous utility functions that are lacking
 * in the standard library. In fact, the title "jim" is an inspiration
 * from "vim" that means "java improved".
 * <p>
 * Be advised that any function within this class is subject to removal
 * or relocation. A deprecation warning will be annotated on these members
 * at least one library version before their removal.
 *
 * @author Maxim Kasyanenko
 * @since 1.0
 */
public class JimStatics {

    /** The best precision available for a 32-bit IEEE-754 floating point number in base 10. */
    private static final float  F32_EPSILON = 1e-7f;

    /** The best precision available for a 64-bit IEEE-754 floating point number in base 10. */
    private static final double F64_EPSILON = 1e-16d;

    /**
     * Determines whether the two given floating point values are virtually
     * equivalent. This method is equivalent to {@code Math.abs(a - b) <= 1e-7f}.
     *
     * @param a the first number.
     * @param b the second number.
     * @return true if the two numbers are virtually the same; false otherwise.
     */
    public static boolean equal (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON;
    }

    /**
     * Determines whether the two given floating point values are virtually
     * equivalent. This method is equivalent to {@code Math.abs(a - b) <= 1e-16d}.
     *
     * @param a the first number.
     * @param b the second number.
     * @return true if the two numbers are virtually the same; false otherwise.
     */
    public static boolean equal (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON;
    }

    /**
     * Clamps the given double value in {@code [min, max]}.
     *
     * @param v the value to clamp.
     * @param min the lower value of the interval.
     * @param max the higher value of the interval.
     * @return the clamped value.
     */
    public static double clamp (double v, double min, double max) {
        if (min > max)
            return clamp (v, max, min);
        return Math.max (Math.min (v, max), min);
    }

    /**
     * Clamps the given integer value in {@code [min, max]}.
     *
     * @param v the value to clamp.
     * @param min the lower value of the interval.
     * @param max the higher value of the interval.
     * @return the clamped value.
     */
    public static int clamp (int v, int min, int max) {
        if (min > max)
            return clamp (v, max, min);
        return Math.max (Math.min (v, max), min);
    }

    /**
     * Equivalent: {@code obj == null ? fallback : obj}.
     * <p>
     * This is fundamentally equivalent to
     * {@link java.util.Objects#requireNonNullElse(Object, Object)}
     * except with a shorter method signature.
     *
     * @param obj the object required to not be null.
     * @param fallback the fallback of the object is null.
     * @param <T> the generic type.
     * @return {@code obj} if {@code obj != null}, {@code fallback} otherwise.
     */
    public static <T> T nonull (T obj, T fallback) {
        return obj == null ? fallback : obj;
    }
}
