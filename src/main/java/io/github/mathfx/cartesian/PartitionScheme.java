package io.github.mathfx.cartesian;

import io.github.mkmax.util.math.Float64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PartitionScheme {
    DECIMAL {
        @Override
        public List<PartitionIndex> partition (
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
            final List<PartitionIndex> indices = new ArrayList<> (total);

            int maj = 0;
            for (int min = 0; min < total; ++min) {
                PartitionType type   = PartitionType.MINOR;
                double minpos = minorStart + min * minorStep;
                double majpos = majorStart + maj * majorStep;

                if (Float64.strictEq (minpos, majpos)) {
                    type = PartitionType.MAJOR;
                    ++maj;
                }

                if (Float64.strictEq (minpos, 0d))
                    type = PartitionType.ORIGIN;

                indices.add (new PartitionIndex (type, minpos));
            }

            return indices;
        }
    };

    public List<PartitionIndex> partition (
        double iBegin,
        double iEnd,
        double fBegin,
        double fEnd,
        double ppu)
    {
        throw new IllegalStateException ("partition scheme must implement the partition(...) function");
    }
}
