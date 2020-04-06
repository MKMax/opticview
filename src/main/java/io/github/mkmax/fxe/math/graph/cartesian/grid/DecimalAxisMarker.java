package io.github.mkmax.fxe.math.graph.cartesian.grid;

import io.github.mkmax.util.math.Floats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DecimalAxisMarker implements AxisMarker {

    public enum Scheme {
        FRAGMENTS,
        PARTITION
    }

    private Scheme scheme;
    private double fpu        = 128d;
    private int    partitions = 8;

    public DecimalAxisMarker (Scheme scheme) {
        setScheme (scheme);
    }

    public Scheme getScheme () {
        return scheme;
    }

    public void setScheme (Scheme nScheme) {
        scheme = Objects.requireNonNull (nScheme);
    }

    public double getFpu () {
        return fpu;
    }

    public void setFpu (double nFpu) {
        fpu = Math.max (1d, nFpu);
    }

    public int getPartitions () {
        return partitions;
    }

    public void setPartitions (int nPartitions) {
        partitions = Math.max (1, nPartitions);
    }

    @Override
    public List<Mark> getMarks (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd)
    {
        double min;
        double max;
        double factor;

        if (scheme == Scheme.FRAGMENTS) {
            min = Math.min (fragmentBegin, fragmentEnd);
            max = Math.max (fragmentBegin, fragmentEnd);
            factor = fpu;
        }
        else {
            min = Math.min (intervalBegin, intervalEnd);
            max = Math.max (intervalBegin, intervalEnd);
            factor = partitions;
        }

        final double unit     = (max - min) / factor;
        final double log10    = Math.ceil (Math.log10 (unit));
        final double pow10    = Math.pow (10, log10);
        final double normUnit = unit / pow10;

        double majorStep;
        double minorStep;

        if (normUnit <= 0.2d) {
            majorStep = 0.2d * pow10;
            minorStep = 0.25d * majorStep;
        }
        else if (normUnit <= 0.5d) {
            majorStep = 0.5d * pow10;
            minorStep = 0.2d * majorStep;
        }
        else {
            majorStep = pow10;
            minorStep = 0.2d * majorStep;
        }

        double majorStart;
        double minorStart;

        if (min < 0) {
            majorStart = min - (min % majorStep);
            minorStart = min - (min % minorStep);

            if (Floats.strictEq (min, majorStart))
                majorStart += majorStep;
            if (Floats.strictEq (min, minorStart))
                minorStart += minorStep;
        }
        else {
            majorStart = min + (majorStep - min % majorStep);
            minorStart = min + (minorStep - min % minorStep);
        }

        if (minorStart >= max)
            return Collections.emptyList ();

        final int        markCount = (int) Math.ceil ((max - minorStart) / minorStep);
        final List<Mark> result    = new ArrayList<> (markCount);

        int maj = 0, i = 0;
        while (i < markCount) {
            final double  majLocation = majorStart + (majorStep * maj);
            final double  minLocation = minorStart + (minorStep * i);
            final boolean major = Floats.leanEq (majLocation, minLocation);

            if (major)
                ++maj;

            final Mark mark = new Mark (
                major ? Degree.MAJOR : Degree.MINOR,
                minLocation
            );

            ++i;
        }

        return result;
    }
}
