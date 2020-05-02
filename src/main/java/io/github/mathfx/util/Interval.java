package io.github.mathfx.util;

public class Interval {

    public final double min;
    public final double max;

    public Interval (double a, double b) {
        min = Math.min (a, b);
        max = Math.max (a, b);
    }

    public double distance () {
        return max - min;
    }
}
