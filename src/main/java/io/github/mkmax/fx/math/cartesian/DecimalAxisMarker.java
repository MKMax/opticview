package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.fx.math.cartesian.AxisMark.Type;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.Range2d;
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
public class DecimalAxisMarker implements AxisMarker {

    private static final double FPU_MIN = 1d;
    private static final double FPU_MAX = 65536d;

    private AxisMark[] cachedMajorMarks;
    private AxisMark[] cachedMinorMarks;

    /* 128 for no particular reason, just seems like a nice number */
    private double fpu = 128d;

    public DecimalAxisMarker (double fpu) {
        setFragmentsPerUnit (fpu);
    }

    public DecimalAxisMarker () {
        /* initial state reached */
    }

    public void setFragmentsPerUnit (double nFpu) {
        fpu = Math.max (FPU_MIN, Math.min (FPU_MAX, nFpu));
    }

    public double getFragmentsPerUnit () {
        return fpu;
    }

    @Override
    public Iterable<AxisMark> getMajorMarks (
        Range2d unitRange,
        Range2d fragmentRange)
    {
        final double unitNumericRange     = unitRange.absRange ();
        final double fragmentNumericRange = fragmentRange.absRange ();

        /* Obtain the current fragments per unit (C-FPU) defined
         * by the unit space (unitRange) & fragment space
         * (fragmentRange).
         *
         * NOTE: 'cfpu' is guaranteed to be positive because we use
         * the absRange() function of the DoubleRange class.
         */
        final double cfpu = fragmentNumericRange / unitNumericRange;

        /* Compute the exact number of units it would take
         * to create an axis point exactly 'fpu' in length.
         * This will set us up to compute the desired units.
         *
         * NOTE: 'unitsInMin' is also guaranteed to be positive since
         * 'fpu' is bound from below by +1.0 and 'fpu' is guaranteed to
         * be positive from the expression above. This ensures that we
         * can use it in logarithms safely.
         */
        final double unitsInMin = fpu / cfpu;

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
         * multiply it with 'fpu' to obtain the actual unit we would
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
         * side of 'unitRange.min' that is a multiple of 'step'. Using the
         * computed 'start', we emit it and continue incrementing by step until
         * we reach a value >= 'unitRange.max', emitting each point we iterate
         * over.
         */
        double start;

        /* Because of how modular arithmetic works with negatives in java, we must
         * have different computations when the sign is different for 'unitRange.min'.
         * When 'unitRange.min' is positive, modulus works as expected and
         * produces a positive result. However, when 'unitRange.min' is negative,
         * i.e. < 0, the remainder produced by the modulus operation actually ends
         * up being negative as well (which is technically valid, but does not truly follow
         * the definition we see in number theory).
         *
         * Additional note: because the axes should only be in the range
         * (unitRange.min, unitRange.max) exclusively, we need to ensure
         * that 'start' != 'unitRange.min' which may happen if 'unitRange.min'
         * is perfectly divisible by 'step'.
         */
        if (unitRange.min < 0)
            start = unitRange.min - (unitRange.min % step);
        else
            /* Although this computation here should already ensure
             * that 'start' != 'realAxisRange.min', the way floating
             * point numbers work, the expression 'unitRange.min % step'
             * could produce a result that is too similar to 'unitRange.min'
             * in such a way that when computing the 'axisCount' further
             * on, we end up actually including 'unitRange.min'.
             */
            start = unitRange.min + (step - unitRange.min % step);

        /* ensure the criteria mentioned above is met
         * by simply adding 'step' if needed.
         */
        if (FloatingPoint.strictEq (unitRange.min, start))
            start += step;

        /* Trivially, if 'start' is actually ahead (or equal to) the end of
         * our window/function space, there will be no major axis points
         * emitted so we may safely return an empty iterator.
         */
        if (start >= unitRange.max)
            return ArrayIterable.empty ();

        /* Straight forward intrinsic computation of the total number of axes we
         * will iterate over so that we may pre-allocate all of the necessary
         * memory. The only slightly obscure thing here is that we use ceil(), but
         * this is simply to ensure that we keep realAxisRange.max from being an
         * axis point since it is out of our range of desired points.
         *
         * NOTE: We don't have to check to ensure that 'markCount' is negative since
         * the difference 'unitRange.max - start' is guaranteed to be positive
         * by the check above and 'step' is always positive as it is composed of
         * a positive constant multiplied by variable whose function is positive
         * on all real numbers.
         */
        final int markCount = (int) Math.ceil ((unitRange.max - start) / step);

        /* Caching already allocated axis point array in the hopes of saving some
         * performance as this function is expected to be called in rather rapid
         * succession (for example, when rendering a graph multiple times per second).
         */
        if (cachedMajorMarks == null || cachedMajorMarks.length < markCount) {
            /* Admittedly this can be improved a lot more, but we need to write
             * a special array buffer type that would provide direct access to any
             * index in some "resizable" array which java's ArrayList does not
             * allow us to do. This is especially inefficient considering that we
             * may still be re-allocating the array constantly if axisCount continues
             * to increment on each call.
             */
            cachedMajorMarks = new AxisMark[markCount];
        }

        for (int i = 0; i < markCount; ++i) {
            final Type    type            = Type.MAJOR;
            final double  location        = start + (step * i);
            final boolean overlapPossible = false; /* overlapping only applies to minor axes */

            /* TODO: Possible optimization where AxisMark's class definition removes
             *       the final qualifier to enable reuse of already existing AxisMark
             *       instances.
             */
            cachedMajorMarks[i] = new AxisMark (
                type,
                location,
                overlapPossible
            );
        }

        /* Again, this area may also be improved as we're unnecessarily creating
         * a new instance of ArrayIterable each time this function is called.
         */
        return new ArrayIterable<> (cachedMajorMarks, markCount);
    }

    @Override
    public Iterable<AxisMark> getMinorMarks (
        Range2d unitRange,
        Range2d fragmentRange)
    {
        /* WARNING: The following comments may be slightly outdated as I have
         * refactored the majority of how the system actually works. The part
         * that I would like any reader to avoid is "overlapping". This used to
         * have meaning before the refactor when the marker computations were
         * closely coupled with the graph view. This is no longer the case and
         * the axis markers are no longer supposed to be responsible for overlapping.
         * This is left up to the graph renderer to sort out. Because of this,
         * the definition of the AxisMark class has been extended to hint at
         * the possibility of an overlap so that the graph renderer can display
         * the grid properly.
         */

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
        final double unitNumericRange     = unitRange.absRange ();
        final double fragmentNumericRange = fragmentRange.absRange ();

        /* See identical comments in computeMajorPoints(...) */
        final double cfpu            = fragmentNumericRange / unitNumericRange;
        final double unitsInMin      = fpu / cfpu;
        final double log10           = Math.ceil (Math.log10 (unitsInMin));
        final double pow10           = Math.pow (10, log10);
        final double normalizedUnits = unitsInMin / pow10;

        /* Here's where the crucial difference is. Again, I did not invest much
         * time into figuring out how to optimize computing minor points on
         * their own just yet (will possibly come back to it later).
         *
         * TODO: perhaps optimize computeMinorPoints(...)
         *
         * Either way, in computeMajorPoints, we only cared about the "major" axis mark
         * which has one defined pattern that it follows. Minor axes depend on the
         * computation result from the major axes to ensure that there is no overlapping
         * marks emitted and to make sure that the 'minorStep' is adjusted
         * accordingly to fit into the 'majorStep'.
         *
         * Because 'majorStep' can either be a factor of 0.2 (fifths), 0.5 (halves), or
         * 1.0 (wholes), our corresponding 'minorStep' must either be 0.25 (quarters),
         * or 0.2 (fifths) AND 0.2 (fifths) respectively.
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
        if (unitRange.min < 0) {
            majorStart = unitRange.min - (unitRange.min % majorStep);
            minorStart = unitRange.min - (unitRange.min % minorStep);

            if (FloatingPoint.strictEq (unitRange.min, majorStart))
                majorStart += majorStep;
            if (FloatingPoint.strictEq (unitRange.min, minorStart))
                minorStart += minorStep;
        }
        else {
            majorStart = unitRange.min + (majorStep - unitRange.min % majorStep);
            minorStart = unitRange.min + (minorStep - unitRange.min % minorStep);
        }

        /* We only check 'minorStart' since that is what ultimately matters.
         * For additional comments, see the comments in computeMajorPoints(...)
         */
        if (minorStart >= unitRange.max)
            return ArrayIterable.empty ();

        /* Similar situation as described by the identical comments in the
         * computeMajorPoints(...) at this point in the function.
         */
        final int markCount = (int) Math.ceil ((unitRange.max - minorStart) / minorStep);

        /* See equivalent comment in computeMajorAxis(...) describing the problems
         * with this approach of caching.
         */
        if (cachedMinorMarks == null || cachedMinorMarks.length < markCount)
            cachedMinorMarks = new AxisMark[markCount];

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
        int maj = 0, i = 0;
        while (i < markCount) {
            final double majLocation = majorStart + (majorStep * maj);

            final Type    type             = Type.MINOR;
            final double  minLocation      = minorStart + (minorStep * i);
                  boolean overlapPossible  = false;

            /* If there is a possibility of overlapping, we must flag it
             * so that the graph renderer can take appropriate action when
             * rendering the grid.
             */
            if (FloatingPoint.leanEq (majLocation, minLocation)) {
                overlapPossible = true;
                ++maj;
            }

            cachedMinorMarks[i++] = new AxisMark (
                type,
                minLocation,
                overlapPossible
            );
        }

        /* See the comment about this in computeMajorPoints(...) regarding problems. */
        return new ArrayIterable<> (cachedMinorMarks, markCount);
    }

}
