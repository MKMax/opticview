package io.github.mkmax.math;

public class DoubleRange {

    public final double min;
    public final double max;

    public DoubleRange (double pMin, double pMax) {
        if (pMin > pMax)
            throw new IllegalArgumentException ();
        min = pMin;
        max = pMax;
    }

    public double range () {
        return max - min;
    }

}
