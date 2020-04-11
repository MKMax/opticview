package io.github.mkmax.util.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Float32 {

    public static final float F32_EPSILON_STRICT = 1e-6f;
    public static final float F32_EPSILON_LEAN = 1e-5f;

    public static boolean strictEq (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON_STRICT;
    }

    public static boolean leanEq (float a, float b) {
        return Math.abs (a - b) <= F32_EPSILON_LEAN;
    }

    public static float clamp (float v, float l, float r) {
        return Math.max (Math.min (r, v), l);
    }

    public static int elements (float start, float end, float step) {
        return (int) Math.floor ((end - start) / step) + 1;
    }

    public static List<Float> generate (
        float start,
        float end,
        float step) {
        if (start < end && step <= 0 ||
            start > end && step >= 0)
            return Collections.emptyList ();
        final int size = elements (start, end, step);
        final List<Float> result = new ArrayList<> (elements (start, end, step));
        for (int i = 0; i < size; ++i)
            result.add (start + i * step);
        return result;
    }

    public static v
}