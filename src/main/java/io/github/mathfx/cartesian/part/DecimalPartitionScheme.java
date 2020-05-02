package io.github.mathfx.cartesian.part;

import io.github.mkmax.util.math.Float64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecimalPartitionScheme implements PartitionScheme {

    private static final class Holder {
        private static final DecimalPartitionScheme INSTANCE = new DecimalPartitionScheme ();
    }

    public static DecimalPartitionScheme getInstance () {
        return Holder.INSTANCE;
    }

    private DecimalPartitionScheme () {
    }

    @Override
    public List<Index> partition (
        double iBegin,
        double iEnd,
        double fBegin,
        double fEnd,
        double ppu)
    {
        final double iMin = Math.min (iBegin, iEnd);
        final double iMax = Math.max (iBegin, iEnd);

        final double fMin = Math.min (fBegin, fEnd);
        final double fMax = Math.max (fBegin, fEnd);



        final double appu  = (fMax - fMin) / (iMax - iMin);
        final double unit  = ppu / appu;
        final double log10 = Math.ceil (Math.log10 (unit));
        final double pow10 = Math.pow (10, log10);
        final double norm  = unit / pow10;



        double majorStep, minorStep;

        if (norm <= 0.2d) {
            majorStep = 0.2d * pow10;
            minorStep = 0.25d * majorStep;
        }
        else if (norm <= 0.5d) {
            majorStep = 0.5d * pow10;
            minorStep = 0.2d * majorStep;
        }
        else {
            majorStep = 1.0d * pow10;
            minorStep = 0.2d * majorStep;
        }


        double majorStart, minorStart;

        if (iMin < 0) {
            majorStart = iMin - (iMin % majorStep);
            minorStart = iMin - (iMin % minorStep);
        }
        else {
            majorStart = iMin + (majorStep - iMin % majorStep);
            minorStart = iMin + (minorStep - iMin % minorStep);
        }


        if (Float64.strictEq (iMin, majorStart))
            majorStart += majorStep;
        if (Float64.strictEq (iMin, minorStart))
            minorStart += minorStep;

        if (minorStart >= iMax)
            return Collections.emptyList ();

        final int total = (int) Math.floor ((iMax - minorStart) / minorStep) + 1;
        final List<Index> indices = new ArrayList<> (total);

        int maj = 0;
        for (int min = 0; min < total; ++min) {
            Type   type   = Type.MINOR;
            double minpos = minorStart + min * minorStep;
            double majpos = majorStart + maj * majorStep;

            if (Float64.strictEq (minpos, majpos)) {
                type = Type.MAJOR;
                ++maj;
            }

            if (Float64.strictEq (minpos, 0d))
                type = Type.ORIGIN;

            indices.add (new Index (type, minpos));
        }

        return indices;
    }
}
