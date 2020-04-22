package io.github.mkmax.fxe.math.graph.cartesian.partitioning;

import io.github.mkmax.util.math.Float64;

public class DecimalPartition implements Partition {

    public static final DecimalPartition INSTANCE = new DecimalPartition ();

    private DecimalPartition () {
    }

    @Override
    public void calc (
        Receiver rcv,
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu)
    {
        if (rcv == null)
            return;



        final double intervalMin = Math.min (intervalBegin, intervalEnd);
        final double intervalMax = Math.max (intervalBegin, intervalEnd);

        final double fragmentMin = Math.min (fragmentBegin, fragmentEnd);
        final double fragmentMax = Math.max (fragmentBegin, fragmentEnd);



        final double afpu  = (fragmentMax - fragmentMin) / (intervalMax - intervalMin);
        final double unit  = fpu / afpu;
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

        if (intervalMin < 0) {
            majorStart = intervalMin - (intervalMin % majorStep);
            minorStart = intervalMin - (intervalMin % minorStep);
        }
        else {
            majorStart = intervalMin + (majorStep - intervalMin % majorStep);
            minorStart = intervalMin + (minorStep - intervalMin % minorStep);
        }


        if (Float64.strictEq (intervalMin, majorStart))
            majorStart += majorStep;
        if (Float64.strictEq (intervalMin, minorStart))
            minorStart += minorStep;

        if (minorStart >= intervalMax)
            return;



        final int total = (int) Math.floor ((intervalMax - minorStart) / minorStep) + 1;

        int maj = 0;
        for (int min = 0; min < total; ++min) {
            Type   type   = Type.MINOR;
            double minpos = minorStart + min * minorStep;
            double majpos = majorStart + maj * majorStep;

            if (Float64.leanEq (minpos, majpos)) {
                type = Type.MAJOR;
                ++maj;
            }

            if (Float64.leanEq (majpos, 0d))
                type = Type.ORIGIN;

            rcv.rcv (type, minpos);
        }
    }
}
