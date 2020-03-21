package io.github.mkmax.util.math;

import java.util.Objects;

public class DoubleRange {

    public static final class Lerp {

        /* just save the ranges in case */
        private final DoubleRange from;
        private final DoubleRange to;

        /* compute the linear interpolation function (literally y = mx + b) */
        private final double m; // slope or gradient
        private final double b; // y-intercept

        public Lerp (DoubleRange from, DoubleRange to) {
            this.from = Objects.requireNonNull (from);
            this.to   = Objects.requireNonNull (to);

            /* don't worry about divide by zero, floating points will handle it */
            m = to.range () / from.range ();
            b = to.min - m * from.min;
        }

        public DoubleRange getFrom () {
            return from;
        }

        public DoubleRange getTo () {
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

    public final double min;
    public final double max;

    public DoubleRange (double pMin, double pMax) {
        if (pMin > pMax) {
            min = pMax;
            max = pMin;
        }
        else {
            min = pMin;
            max = pMax;
        }
    }

    public double range () {
        /* guaranteed to be positive from checks performed in constructor. */
        return max - min;
    }

}
