package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.math.DoubleRange;

import java.util.Iterator;

public class StandardCartesianAxisProfile extends AbstractCartesianAxisProfile {

    private static final double MIN_FPU = 1d;

    private double mfpu;

    public StandardCartesianAxisProfile (double mfpu) {
        setMinimumFragmentsPerUnit (mfpu);
    }

    public void setMinimumFragmentsPerUnit (double nMfpu) {
        mfpu = Math.max (MIN_FPU, nMfpu);
    }

    public double getMinimumFragmentsPerUnit () {
        return mfpu;
    }

    @Override
    public Iterator<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {
        /* Obtain the current fragments per
         * unit (FPU) defined by the window
         * (realAxisRange) & viewport (mappedAxisRange).
         */
        final double fpu = mappedAxisRange.range () / realAxisRange.range ();

        /* Compute the exact number of units it would take
         * to create an axis point exactly mfpu in length.
         * This will set us up to compute the desired units.
         */
        final double unitsInMin = mfpu / fpu;

        /* Because the pattern in the axis points repeats every
         * power of 10, we will compute it and take the ceiling
         * to set us up for "normalizing" the unitsInMin we
         * calculated above. We use ceiling instead of floor to
         * ensure that our "normalized" units will be in the range
         * of (0, 1) exclusive.
         */
        final double log10 = Math.ceil (Math.log10 (unitsInMin));

        /* Compute this pow10 so we don't have to keep doing it over and
         * over again later. This will be used to compute the 'normalizedUnits',
         * the 'step', and possibly a few other things later.
         */
        final double pow10 = Math.pow (10, log10);

        /* We put the data computed above together to normalize our
         * unit size. Because this is normalized to (0, 1) we can easily
         * multiply it with 'mfpu' to obtain the actual unit we would
         * like to be emitted for each major axis. Of course, the current
         * result may be some weird decimal that doesn't make sense. We
         * continue on to make the units more user friendly by ensuring
         * that the unit is either ceil-ed up to round it out, in halves (1/2)
         * or in fifths (1/5). Beyond fifths (1/5) we would encounter more
         * unreasonable decimals and by the time we reach the tenths (1/10)
         * 'log10' above would put us back well above the halves.
         */
        final double normalizedUnits = unitsInMin / pow10;

        /* The step is a window-space (or function-space) value that determines
         * the separation of each major axis as if it were on a window-space
         * graph. This value can easily then be converted to the viewport
         * scale at which point we will have our separation in fragments for
         * each major axis. That would be half of the job done.
         */
        double step;

        if (normalizedUnits <= 0.2d)      /* normalizedUnits <= (1d / 5d) */
            step = 0.2d * pow10;
        else if (normalizedUnits <= 0.5d) /* normalizedUnits <= (1d / 2d) */
            step = 0.5d * pow10;
        else                              /* normalizedUnits <= 1d        */
            step = pow10;


    }

    @Override
    public Iterator<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {

    }

}
