package io.github.mkmax.util.math;

public class Intervald {

    public final double a;
    public final double b;

    public final double min;
    public final double max;

    public Intervald (double pA, double pB) {
        a = pA;
        b = pB;

        min = Math.min (a, b);
        max = Math.max (a, b);
    }

    /* computes |a - b| */
    public double range () {
        return max - min;
    }
}