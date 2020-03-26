package io.github.mkmax.util.math;

import java.util.Objects;

public class Range2d {

    public static final class Project {

        /* just save the ranges in case */
        private final Range2d from;
        private final Range2d to;

        /* compute the linear interpolation function (literally y = mx + b) */
        private final double m; // slope or gradient
        private final double b; // y-intercept

        public Project (Range2d from, Range2d to) {
            this.from = Objects.requireNonNull (from);
            this.to   = Objects.requireNonNull (to);

            /* don't worry about divide by zero, floating points will handle it */
            m = to.range () / from.range ();
            b = to.a - m * from.a;
        }

        public Range2d getFrom () {
            return from;
        }

        public Range2d getTo () {
            return to;
        }

        public double project (double value) {
            return m * value + b;
        }

        public double invert (double value) {
            /* here also, don't worry about divide by zero, floating points will handle it */
            return (value - b) / m;
        }
    }

    public final double a;
    public final double b;

    public final double min;
    public final double max;

    public Range2d (double pA, double pB) {
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