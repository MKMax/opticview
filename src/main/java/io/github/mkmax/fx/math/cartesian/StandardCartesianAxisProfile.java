package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.DoubleRange.Lerp;
import io.github.mkmax.util.math.DoubleRange;

public class StandardCartesianAxisProfile extends AbstractCartesianAxisProfile {

    private static final Iterable<CartesianAxisPoint> EMPTY = ArrayIterable.empty ();

    private static final double MIN_FPU = 1d;
    private static final int    MIN_CACHE_SIZE = 8;

    private ArrayIterable<CartesianAxisPoint> cachedAxisPointArray;
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
    public Iterable<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {
        final double realAxisNumericRange   = realAxisRange.range ();
        final double mappedAxisNumericRange = mappedAxisRange.range ();

        /* Obtain the current fragments per
         * unit (FPU) defined by the window
         * (realAxisRange) & viewport (mappedAxisRange).
         */
        final double fpu = mappedAxisNumericRange / realAxisNumericRange;

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
         * of (0, 1].
         */
        final double log10 = Math.ceil (Math.log10 (unitsInMin));

        /* Compute this pow10 so we don't have to keep doing it over and
         * over again later. This will be used to compute the 'normalizedUnits',
         * the 'step', and possibly a few other things later.
         */
        final double pow10 = Math.pow (10, log10);

        /* We put the data computed above together to normalize our
         * unit size. Because this is normalized to (0, 1] we can easily
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

        /* Technical note: here, 'step' is guaranteed to be >= 'unitsInMin' because
         * we implicitly declare that the coefficient that we will multiply by would
         * be >= 'normalizedUnits' in the if statements.
         *
         * Thus 'step' / 'pow10' >= 'normalizedUnits' = 'unitsInMin' / 'pow10', then
         * 'step' >= 'normalizedUnits' * 'pow10' = 'unitsInMin', then
         * 'step' >= 'unitsInMin'
         *
         * This may be useful information for understanding why following equations
         * hold true.
         */
        if (normalizedUnits <= 0.2d)      /* normalizedUnits <= (1d / 5d) */
            step = 0.2d * pow10;
        else if (normalizedUnits <= 0.5d) /* normalizedUnits <= (1d / 2d) */
            step = 0.5d * pow10;
        else                              /* normalizedUnits <= 1d        */
            step = pow10;

        /* Now that we have the step (in window/function space of course), all we
         * need to do now is calculate at which points in the window/function space
         * to start and stop marking axes. Once we find these two values, it is
         * trivial to create an iterator that yields each one of the axis points.
         * The main challenge in this case is to ensure that, if the window/function
         * space crosses 0, it must be included. In other words, our axes must be
         * aligned with the primary axis: the zero axis.
         */

        /* Using the 'step' we computed previously, we can now determine the
         * location in the window/function space at which we can start emitting
         * axis points. We do this by finding the first value to the positive
         * side of 'realAxisRange.min' that is a multiple of 'step'. Using the
         * computed 'start', we emit it and continue incrementing by step until
         * we reach a value >= 'realAxisRange.max', emitting each point we iterate
         * over.
         */
        double start;

        /* Because of how modular arithmetic works with negatives in java, we must
         * have different computations when the sign is different for 'realAxisRange.min'.
         * When 'realAxisRange.min' is positive, modulus works as expected and
         * produces a positive result. However, when 'realAxisRange.min' is negative,
         * i.e. < 0, the remainder produced by the modulus operation actually ends
         * up being negative as well (which is technically valid, but does not truly follow
         * the definition we see in number theory).
         */
        if (realAxisRange.min < 0)
            start = realAxisRange.min - (realAxisRange.min % step);
        else
            start = realAxisRange.min + (step - realAxisRange.min % step);

        /* Trivially, if 'start' is actually ahead (or equal to) the end of
         * our window/function space, there will be no major axis points
         * emitted so we may safely return an empty iterator.
         */
        if (start >= realAxisRange.max)
            return EMPTY;

        /* Straight forward intrinsic computation of the total number of axes we
         * will iterate over so that we may pre-allocate all of the necessary
         * memory.
         */
        final int axisCount = (int) Math.floor ((realAxisRange.max - start) / step + 1);

        /* Caching already allocated axis point array in the hopes of saving some
         * performance as this function is expected to be called in rather rapid
         * succession (for example, when rendering a graph multiple times per second).
         */
        if (cachedAxisPointArray == null)
            cachedAxisPointArray = new ArrayList<> (axisCount);

        if (cachedAxisPointArray.size () < axisCount)
            cachedAxisPointArray.ensureCapacity (axisCount);

        /* We will need this to compute the viewport value of the axis point */
        final Lerp winToVp = new Lerp (realAxisRange, mappedAxisRange);

        for (int i = 0; i < axisCount; ++i) {
            double pWindowSpace   = start + (step * i);
            double pViewportSpace = winToVp.project (pWindowSpace);

            cachedAxisPointArray[i] = new CartesianAxisPoint (pWindowSpace, pViewportSpace);
        }

        /* Yes, we have finally completed this monumental task */
        return new ArrayIterable<> (cachedAxisPointArray, axisCount);
    }

    @Override
    public Iterable<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {
        return null;
    }

}
