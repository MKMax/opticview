package io.github.mkmax.util.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Float64 {

    public static final double F64_EPSILON_STRICT = 1e-15d;
    public static final double F64_EPSILON_LEAN   = 1e-14d;

    public static boolean strictEq (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON_STRICT;
    }

    public static boolean leanEq (double a, double b) {
        return Math.abs (a - b) <= F64_EPSILON_LEAN;
    }

    public static double clamp (double v, double l, double r) {
        return Math.max (Math.min (r, v), l);
    }

    public static int elements (double start, double end, double step) {
        return (int) Math.floor ((end - start) / step) + 1;
    }

    public static List<Double> generate (
        double start,
        double end,
        double step)
    {
        if (start < end && step <= 0 ||
            start > end && step >= 0)
            return Collections.emptyList ();
        final int size = elements (start, end, step);
        final List<Double> result = new ArrayList<> (elements (start, end, step));
        for (int i = 0; i < size; ++i)
            result.add (start + i * step);
        return result;
    }
}
