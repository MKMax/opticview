package io.github.mathfx.util;

public class NumberUtil {

    public static double clamp (double v, double min, double max) {
        if (min > max)
            return clamp (v, max, min);
        return Math.max (Math.min (v, max), min);
    }

}
