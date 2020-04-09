package io.github.mkmax.util.math;

public class Intervald {

    public final double start;
    public final double end;

    public final double min;
    public final double max;

    public Intervald (
        double pStart,
        double pEnd)
    {
        start = pStart;
        end   = pEnd;
        min   = Math.min (start, end);
        max   = Math.max (start, end);
    }

    public boolean in (double val) {
        return min <= val && val <= max;
    }

    public boolean equals (Intervald other) {
        return other != null &&
               Double.doubleToRawLongBits (other.start) == Double.doubleToRawLongBits (start) &&
               Double.doubleToRawLongBits (other.end)   == Double.doubleToRawLongBits (end);
    }

    @Override
    public int hashCode () {
        long res = 0L;
        res ^= Double.doubleToRawLongBits (start);
        res <<= 23L;
        res ^= Double.doubleToRawLongBits (end);
        return (int) ((res >> 32L) ^ (res));
    }

    @Override
    public boolean equals (Object obj) {
        return obj instanceof Intervald && equals ((Intervald) obj);
    }
}
