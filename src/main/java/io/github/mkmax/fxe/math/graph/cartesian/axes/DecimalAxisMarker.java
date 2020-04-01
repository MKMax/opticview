package io.github.mkmax.fxe.math.graph.cartesian.axes;

import io.github.mkmax.util.data.EmptyIterable;
import io.github.mkmax.util.data.RangeIterable;
import io.github.mkmax.util.math.FloatingPoint;

import java.util.ArrayList;

/* non-javadoc
 *
 * An emulation of an axis profile that is similar to the one
 * seen in the, surprisingly good, free online Desmos graphing
 * calculator (https://www.desmos.com/calculator).
 *
 * I'm sure most other calculators use this but I'm most familiar
 * with desmos and that is what I used to derive the computations
 * for the major & minor axis points for this profile.
 */
public class DecimalAxisMarker implements AxisMarker {

    private final ArrayList<AxisMark> majorBuffer = new ArrayList<> ();
    private final ArrayList<AxisMark> minorBuffer = new ArrayList<> ();

    private final RangeIterable<AxisMark> majorIterable = new RangeIterable<> (majorBuffer);
    private final RangeIterable<AxisMark> minorIterable = new RangeIterable<> (minorBuffer);

    private int partitionLimit = 8;

    public DecimalAxisMarker (int partitionLimit) {
        setPartitionLimit (partitionLimit);
    }

    public DecimalAxisMarker () {
        /* initial state reached */
    }

    public int getPartitionLimit () {
        return partitionLimit;
    }

    public void setPartitionLimit (int nPartitionLimit) {
        partitionLimit = Math.max (1, nPartitionLimit);
    }

    public Iterable<AxisMark> getMarks (
        double          intervalBegin,
        double          intervalEnd,
        AxisMark.Degree degree)
    {
        switch (degree) {
        case MAJOR: return getMajorMarks (intervalBegin, intervalEnd);
        case MINOR: return getMinorMarks (intervalBegin, intervalEnd);
        default:    return EmptyIterable.adaptedInstance ();
        }
    }

    private Iterable<AxisMark> getMajorMarks (
        double begin,
        double end)
    {
        /* General computation */
        final double min = Math.min (begin, end);
        final double max = Math.max (begin, end);

        final double unit     = (max - min) / partitionLimit;
        final double log10    = Math.ceil (Math.log10 (unit));
        final double pow10    = Math.pow (10, log10);
        final double normUnit = unit / pow10;

        /* Step computation */
        double step;

        if (normUnit <= 0.2d)
            step = 0.2d * pow10;
        else if (normUnit <= 0.5d)
            step = 0.5d * pow10;
        else
            step = pow10;

        /* Start position computation */
        double start;

        if (min < 0)
            start = min - (min % step);
        else
            start = min + (step - min % step);

        if (FloatingPoint.strictEq (min, start))
            start += step;

        /* Mark computations */
        if (start >= max)
            return EmptyIterable.adaptedInstance ();

        final int markCount = (int) Math.ceil ((max - start) / step);

        if (majorBuffer.size () < markCount) {
            majorBuffer.ensureCapacity (markCount);
            while (majorBuffer.size () < markCount)
                majorBuffer.add (new AxisMark ());
        }

        for (int i = 0; i < markCount; ++i) {
            final AxisMark mark = majorBuffer.get (i);
            mark.setPosition (start + step * i);
            mark.setDegree   (AxisMark.Degree.MAJOR);
            mark.setOverlap  (false);
        }

        return majorIterable.withRange (0, markCount);
    }

    private Iterable<AxisMark> getMinorMarks (
        double begin,
        double end)
    {
        /* General computation */
        final double min = Math.min (begin, end);
        final double max = Math.max (begin, end);

        final double unit     = (max - min) / partitionLimit;
        final double log10    = Math.ceil (Math.log10 (unit));
        final double pow10    = Math.pow (10, log10);
        final double normUnit = unit / pow10;

        /* Compute steps */
        double majorStep;
        double minorStep;

        if (normUnit <= 0.2d) {      /* normalizedUnits <= (1d / 5d) */
            majorStep = 0.2d * pow10;
            minorStep = 0.25d * majorStep;
        }
        else if (normUnit <= 0.5d) { /* normalizedUnits <= (1d / 2d) */
            majorStep = 0.5d * pow10;
            minorStep = 0.2d * majorStep;
        }
        else {                              /* normalizedUnits <= 1d        */
            majorStep = pow10;
            minorStep = 0.2d * majorStep;
        }

        /* Compute start positions */
        double majorStart;
        double minorStart;

        if (min < 0) {
            majorStart = min - (min % majorStep);
            minorStart = min - (min % minorStep);

            if (FloatingPoint.strictEq (min, majorStart))
                majorStart += majorStep;
            if (FloatingPoint.strictEq (min, minorStart))
                minorStart += minorStep;
        }
        else {
            majorStart = min + (majorStep - min % majorStep);
            minorStart = min + (minorStep - min % minorStep);
        }

        /* Obviously if the start is ahead of the end then there are no marks */
        if (minorStart >= max)
            return EmptyIterable.adaptedInstance ();

        /* Generate axis marks */
        final int markCount = (int) Math.ceil ((max - minorStart) / minorStep);

        if (minorBuffer.size () < markCount) {
            minorBuffer.ensureCapacity (markCount);
            while (minorBuffer.size () < markCount)
                minorBuffer.add (new AxisMark ());
        }

        int maj = 0;
        for (int i = 0; i < markCount; ++i) {
            final double majPosition = majorStart + (majorStep * maj);
            final double minPosition = minorStart + (minorStep * i);

            final AxisMark mark = minorBuffer.get (i);
            mark.setDegree   (AxisMark.Degree.MINOR);
            mark.setPosition (minPosition);

            if (FloatingPoint.strictEq (majPosition, minPosition)) {
                mark.setOverlap (true);
                ++maj;
            }
            else
                mark.setOverlap (false);
        }

        return minorIterable.withRange (0, markCount);
    }
}
