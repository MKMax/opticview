package io.github.mkmax.util.math;

public class Segment1dv {

    public final double a;
    public final double b;

    public final double min;
    public final double max;

    public Segment1dv (double pA, double pB) {
        a = pA;
        b = pB;

        min = Math.min (a, b);
        max = Math.max (a, b);
    }

    public boolean in (double val) {
        return min <= val && val <= max;
    }

    /* computes the absolute value of the range (distance between a & b) */
    public double absRange () {
        return max - min;
    }

    /* computes the inverse of the range (a - b), i.e., (start - end) */
    public double invRange () {
        return a - b;
    }

    /* computes the intuitive range (b - a), i.e., (end - start)*/
    public double range () {
        return b - a;
    }
}