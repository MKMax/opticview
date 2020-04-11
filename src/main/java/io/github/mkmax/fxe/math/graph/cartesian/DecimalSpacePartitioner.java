package io.github.mkmax.fxe.math.graph.cartesian;

import io.github.mkmax.util.math.Float64;

import java.util.Collections;
import java.util.List;

public class DecimalSpacePartitioner implements SpacePartitioner {

    @Override
    public List<Double> major (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu)
    {
        return any (
            intervalBegin,
            intervalEnd,
            fragmentBegin,
            fragmentEnd,
            fpu,
            1.0d,
            0.5d,
            0.2d
        );
    }

    @Override
    public List<Double> minor (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu)
    {
        return any (
            intervalBegin,
            intervalEnd,
            fragmentBegin,
            fragmentEnd,
            fpu,
            0.2d,
            0.1d,
            0.05d
        );
    }

    private List<Double> any (
        double intervalBegin,
        double intervalEnd,
        double fragmentBegin,
        double fragmentEnd,
        double fpu,
        double sfWhole,
        double sfHalf,
        double sfFifth)
    {
        final double intervalMin = Math.min (intervalBegin, intervalEnd);
        final double intervalMax = Math.max (intervalBegin, intervalEnd);

        final double fragmentMin = Math.min (fragmentBegin, fragmentEnd);
        final double fragmentMax = Math.max (fragmentBegin, fragmentEnd);



        final double afpu  = (fragmentMax - fragmentMin) / (intervalMax - intervalMin);
        final double unit  = fpu / afpu;
        final double log10 = Math.ceil (Math.log10 (unit));
        final double pow10 = Math.pow (10, log10);
        final double norm  = unit / pow10;



        double step;

        if (norm <= 0.2d)
            step = sfFifth * pow10;
        else if (norm <= 0.5d)
            step = sfHalf * pow10;
        else
            step = sfWhole * pow10;



        double start;

        if (intervalMin < 0)
            start = intervalMin - (intervalMin % step);
        else
            start = intervalMin + (step - intervalMin % step);



        if (Float64.strictEq (intervalMin, start))
            start += step;

        if (start >= intervalMax)
            return Collections.emptyList ();



        return Float64.generate (start, intervalMax, step);
    }
}
