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
}
