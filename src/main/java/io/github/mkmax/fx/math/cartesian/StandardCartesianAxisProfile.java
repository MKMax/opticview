package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.DoubleRange.Lerp;
import io.github.mkmax.util.math.DoubleRange;
import io.github.mkmax.util.math.FloatingPoint;

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
public class StandardCartesianAxisProfile extends AbstractCartesianAxisProfile {

    private CartesianAxisPoint[] cachedMajorPointArray;
    private CartesianAxisPoint[] cachedMinorPointArray;

    public StandardCartesianAxisProfile (double pMfpu) {
        super (pMfpu);
    }

    public StandardCartesianAxisProfile () {
        /* initial state already achieved. */
    }

    @Override
    public Iterable<CartesianAxisPoint> computeMajorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {
        /* very import check to run! */
        if (!wouldComputeMajorAxisPoints ())
            return EMPTY;

        final double mfpu                   = getMinimumFragmentsPerUnit ();
        final double realAxisNumericRange   = realAxisRange.absRange ();
        final double mappedAxisNumericRange = mappedAxisRange.absRange ();

        /* Obtain the current fragments per
         * unit (FPU) defined by the window
         * (realAxisRange) & viewport (mappedAxisRange).
         *
         * NOTE: 'fpu' is guaranteed to be positive because we use
         * the absRange() function of the DoubleRange class.
         */
        final double fpu = mappedAxisNumericRange / realAxisNumericRange;

        /* Compute the exact number of units it would take
         * to create an axis point exactly mfpu in length.
         * This will set us up to compute the desired units.
         *
         * NOTE: 'unitsInMin' is also guaranteed to be positive since
         * 'mfpu' is bound from below by +1.0 and 'fpu' is guaranteed to
         * be positive from the expression above. This ensures that we
         * can use it in logarithms safely.
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
         *
         * Additional note: because the axes should only be in the range
         * (realAxisRange.min, realAxisRange.max) exclusively, we need to ensure
         * that 'start' != 'realAxisRange.min' which may happen if 'realAxisRange.min'
         * is perfectly divisible by 'step'.
         */
        if (realAxisRange.min < 0)
            start = realAxisRange.min - (realAxisRange.min % step);
        else
            /* Although this computation here should already ensure
             * that 'start' != 'realAxisRange.min', the way floating
             * point numbers work, the expression 'realAxisRange.min % step'
             * could produce a result that is too similar to 'realAxisRange.min'
             * in such a way that when computing the 'axisCount' further
             * on, we end up actually including 'realAxisRange.min'.
             */
            start = realAxisRange.min + (step - realAxisRange.min % step);

        /* ensure the criteria mentioned above is met
         * by simply adding 'step' if needed.
         */
        if (FloatingPoint.strictEq (realAxisRange.min, start))
            start += step;

        /* Trivially, if 'start' is actually ahead (or equal to) the end of
         * our window/function space, there will be no major axis points
         * emitted so we may safely return an empty iterator.
         */
        if (start >= realAxisRange.max)
            return EMPTY;

        /* Straight forward intrinsic computation of the total number of axes we
         * will iterate over so that we may pre-allocate all of the necessary
         * memory. The only slightly obscure thing here is that we use ceil(), but
         * this is simply to ensure that we keep realAxisRange.max from being an
         * axis point since it is out of our range of desired points.
         *
         * NOTE: We don't have to check to ensure that 'axisCount' is negative since
         * the difference 'realAxisRange.max - start' is guaranteed to be positive
         * by the check above and 'step' is always positive as it is composed of
         * a positive constant multiplied by variable whose function is positive
         * on all real numbers.
         */
        final int axisCount = (int) Math.ceil ((realAxisRange.max - start) / step);

        /* Caching already allocated axis point array in the hopes of saving some
         * performance as this function is expected to be called in rather rapid
         * succession (for example, when rendering a graph multiple times per second).
         */
        if (cachedMajorPointArray == null || cachedMajorPointArray.length < axisCount) {
            /* Admittedly this can be improved a lot more, but we need to write
             * a special array buffer type that would provide direct access to any
             * index in some "resizable" array which java's ArrayList does not
             * allow us to do. This is especially inefficient considering that we
             * may still be re-allocating the array constantly if axisCount continues
             * to increment on each call.
             */
            cachedMajorPointArray = new CartesianAxisPoint[axisCount];
        }

        /* We will need this to compute the viewport value of the axis point */
        final Lerp winToVp = new Lerp (realAxisRange, mappedAxisRange);

        for (int i = 0; i < axisCount; ++i) {
            double pWindowSpace   = start + (step * i);
            double pViewportSpace = winToVp.project (pWindowSpace);

            cachedMajorPointArray[i] = new CartesianAxisPoint (
                pWindowSpace,
                pViewportSpace
            );
        }

        /* Again, this area may also be improved as we're unnecessarily creating
         * a new instance of ArrayIterable each time this function is called.
         */
        return new ArrayIterable<> (cachedMajorPointArray, axisCount);
    }

    @Override
    public Iterable<CartesianAxisPoint> computeMinorPoints (
        DoubleRange realAxisRange,
        DoubleRange mappedAxisRange)
    {
        /* very important check to run! */
        if (!wouldComputeMinorAxisPoints ())
            return EMPTY;

        /* This function's procedure is mostly a copy-paste of computeMajorPoints
         * since the minor axes are closely related to the major axes. However, we
         * cannot entirely generalize the computeMajorPoints function as there are
         * some critical differences with minor points that cannot really be
         * generalized. If it can, it would take more investment time than desired
         * to achieve the same goal.
         *
         * TODO: Improve this function by decreasing the dependencies on
         *       major axis computations since they're not all needed once
         *       you find the fragments per major axis point.
         */

        final double mfpu                   = getMinimumFragmentsPerUnit ();
        final double realAxisNumericRange   = realAxisRange.absRange ();
        final double mappedAxisNumericRange = mappedAxisRange.absRange ();

        /* See identical comments in computeMajorPoints(...) */
        final double fpu             = mappedAxisNumericRange / realAxisNumericRange;
        final double unitsInMin      = mfpu / fpu;
        final double log10           = Math.ceil (Math.log10 (unitsInMin));
        final double pow10           = Math.pow (10, log10);
        final double normalizedUnits = unitsInMin / pow10;

        /* Here's where the crucial difference is. Again, I did not invest much
         * time into figuring out how to optimize computing minor points on
         * their own just yet (will possibly come back to it later).
         *
         * TODO: perhaps optimize computeMinorPoints(...)
         *
         * Either way, in computeMajorPoints, we only cared about the "macro" axis
         * which has one defined pattern that it follows. Minor axes depend on the
         * computation result from the major axes to ensure that there is no overlapping
         * axis points emitted and to make sure that the 'minorStep' is adjusted
         * accordingly to fit into the 'majorStep'.
         *
         * Because 'majorStep' can either be a factor of 0.2 (fifths), 0.5 (halves), or
         * 1.0 (wholes), our corresponding 'minorStep' must either be 0.25 (quarters),
         * or 0.2 (fifths) and 0.2 (fifths) respectively.
         *
         * majorStep | minorStep
         * ---------------------
         * 0.2         majorStep * 0.25
         * 0.5         majorStep * 0.2
         * 1.0         majorStep * 0.2
         */
        double majorStep;
        double minorStep;

        if (normalizedUnits <= 0.2d) {      /* normalizedUnits <= (1d / 5d) */
            majorStep = 0.2d * pow10;
            minorStep = 0.25d * majorStep;
        }
        else if (normalizedUnits <= 0.5d) { /* normalizedUnits <= (1d / 2d) */
            majorStep = 0.5d * pow10;
            minorStep = 0.2d * majorStep;
        }
        else {                              /* normalizedUnits <= 1d        */
            majorStep = pow10;
            minorStep = 0.2d * majorStep;
        }

        /* See identical comments in computeMajorPoints(...) */
        double majorStart;
        double minorStart;

        /* Again, this part of the function does further re-computation that is
         * probably unnecessary, but will work for now. Especially considering
         * that the major axis might even be disabled, we are doing twice the
         * work for nothing.
         */
        if (realAxisRange.min < 0) {
            majorStart = realAxisRange.min - (realAxisRange.min % majorStep);
            minorStart = realAxisRange.min - (realAxisRange.min % minorStep);

            if (FloatingPoint.strictEq (realAxisRange.min, majorStart))
                majorStart += majorStep;
            if (FloatingPoint.strictEq (realAxisRange.min, minorStart))
                minorStart += minorStep;
        }
        else {
            majorStart = realAxisRange.min + (majorStep - realAxisRange.min % majorStep);
            minorStart = realAxisRange.min + (minorStep - realAxisRange.min % minorStep);
        }

        /* We only check 'minorStart' since that is what ultimately matters.
         * For additional comments, see the comments in computeMajorPoints(...)
         */
        if (minorStart >= realAxisRange.max)
            return EMPTY;

        /* Similar situation as described by the identical comments in the
         * computeMajorPoints(...) at this point in the function. However,
         * now we do not have a guarantee that 'majorAxisCount' is always
         * positive or zero. We ultimately compute the true number of minor
         * axes we must yield right after.
         */
        final int majorAxisCount = (int) Math.ceil ((realAxisRange.max - majorStart) / majorStep);
              int minorAxisCount = (int) Math.ceil ((realAxisRange.max - minorStart) / minorStep);

        /* We do not want to overlap with the major axes. */
        if (wouldComputeMajorAxisPoints ())
            minorAxisCount -= Math.max (0, majorAxisCount);

        /* See equivalent comment in computeMajorAxis(...) describing the problems
         * with this approach of caching.
         */
        if (cachedMinorPointArray == null || cachedMinorPointArray.length < minorAxisCount)
            cachedMinorPointArray = new CartesianAxisPoint[minorAxisCount];

        final Lerp winToVp = new Lerp (realAxisRange, mappedAxisRange);

        /* We create two counters to ensure that we can easily compute both
         * the position of the minor axis point but also the next major
         * axis point so we can avoid overlaps.
         *
         * This is another area where we can improve since we know how many
         * 'minorStep's are in a 'majorStep'.
         *
         * TODO: improve how overlap detection occurs and pre-compute
         *       indices to skip.
         */
        int maj = 0, min = 0, i = 0;
        while (i < minorAxisCount) {
            final double pWindowSpaceMaj = majorStart + (majorStep * maj);
            final double pWindowSpaceMin = minorStart + (minorStep * min);
            ++min;

            /* We can use leanEq() here because the margin between the
             * minor and major axis points might be relatively large and
             * strictEq() would be too harsh and actually force overlapping
             */
            if (FloatingPoint.leanEq (pWindowSpaceMaj, pWindowSpaceMin)) {
                ++maj;
                continue;
            }

            final double pViewportSpace = winToVp.project (pWindowSpaceMin);

            cachedMinorPointArray[i++] = new CartesianAxisPoint (
                pWindowSpaceMin,
                pViewportSpace
            );
        }

        /* See the comment about this in computeMajorPoints(...) regarding problems. */
        return new ArrayIterable<> (cachedMinorPointArray, minorAxisCount);
    }

}
