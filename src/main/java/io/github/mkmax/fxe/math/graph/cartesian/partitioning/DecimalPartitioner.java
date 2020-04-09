package io.github.mkmax.fxe.math.graph.cartesian.partitioning;

import io.github.mkmax.util.math.Floats;

public class DecimalPartitioner implements Partitioner {

    public static final DecimalPartitioner INSTANCE = new DecimalPartitioner ();

    private DecimalPartitioner () {
        /* prevent multiple instances from being created. */
    }

    @Override
    public void partition (
        double   intervalBegin,
        double   intervalEnd,
        double   fragmentBegin,
        double   fragmentEnd,
        double   fragmentsPerPartition,
        Consumer consumer)
    {
        if (consumer == null)
            return;

        /* This algorithm has been refactored multiple times and the
         * grid system's design is on it's third iteration. Some comments
         * may be out of date, but I will try and update them when I can.
         */

        final double intervalMin = Math.min (intervalBegin, intervalEnd);
        final double intervalMax = Math.max (intervalBegin, intervalEnd);

        final double fragmentMin = Math.min (fragmentBegin, fragmentEnd);
        final double fragmentMax = Math.max (fragmentBegin, fragmentEnd);

        /* Obtain the current fragments per unit (C-FPU) defined
         * by the unit space & fragment space.
         *
         * NOTE: 'cfpu' is guaranteed to be positive because we compute
         * the max and min of both ranges.
         */
        final double cfpu = (fragmentMax - fragmentMin) / (intervalMax - intervalMin);

        /* Compute the exact number of units it would take
         * to create an axis point exactly 'fpu' in length.
         * This will set us up to compute the desired units.
         *
         * NOTE: 'unitsInMin' is also guaranteed to be positive since
         * 'fpu' is bound from below by +1.0 and 'fpu' is guaranteed to
         * be positive from the expression above. This ensures that we
         * can use it in logarithms safely.
         */
        final double unitsInPartition = fragmentsPerPartition / cfpu;

        /* Because the pattern in the axis points repeats every
         * power of 10, we will compute it and take the ceiling
         * to set us up for "normalizing" the unitsInMin we
         * calculated above. We use ceiling instead of floor to
         * ensure that our "normalized" units will be in the range
         * of (0, 1].
         */
        final double log10 = Math.ceil (Math.log10 (unitsInPartition));

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
        final double normalizedUnits = unitsInPartition / pow10;

        /* The step is a window-space (or function-space) value that determines
         * the separation of each major axis as if it were on a window-space
         * graph. This value can easily then be converted to the viewport
         * scale at which point we will have our separation in fragments for
         * each major axis. That would be half of the job done.
         */
        double majorStep;
        double minorStep;

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
        if (normalizedUnits <= 0.2d) {
            majorStep = 0.2d * pow10;
            minorStep = 0.25d * majorStep;
        }
        else if (normalizedUnits <= 0.5d) {
            majorStep = 0.5d * pow10;
            minorStep = 0.2d * majorStep;
        }
        else {
            majorStep = pow10;
            minorStep = 0.2d * majorStep;
        }

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
        double majorStart;
        double minorStart;

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
        if (intervalMin < 0) {
            majorStart = intervalMin - (intervalMin % majorStep);
            minorStart = intervalMin - (intervalMin % minorStep);
        }
        else {
            /* Although this computation here should already ensure
             * that 'start' != 'realAxisRange.min', the way floating
             * point numbers work, the expression 'unitRange.min % step'
             * could produce a result that is too similar to 'unitRange.min'
             * in such a way that when computing the 'axisCount' further
             * on, we end up actually including 'unitRange.min'.
             */
            majorStart = intervalMin + (majorStep - intervalMin % majorStep);
            minorStart = intervalMin + (minorStep - intervalMin % minorStep);
        }

        /* ensure the criteria mentioned above is met
         * by simply adding 'step' if needed.
         */
        if (Floats.strictEq (intervalMin, majorStart))
            majorStart += majorStep;
        if (Floats.strictEq (intervalMin, minorStart))
            minorStart += minorStep;

        /* Trivially, if 'start' is actually ahead (or equal to) the end of
         * our window/function space, there will be no major axis points
         * emitted so we may safely return an empty iterator.
         */
        if (minorStart >= intervalMax)
            return;

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
        final int markCount = (int) Math.ceil ((intervalMax - minorStart) / majorStep);

        /* Create the coefficient and constant of the linear equation used to map
         * the unit space into fragment space.
         */
        final double Mx = (fragmentEnd - fragmentBegin) / (intervalEnd - intervalBegin);
        final double Kx = fragmentBegin - Mx * intervalBegin;

        int maj = 0;
        for (int i = 0; i < markCount; ++i) {
            final double majUnitPosition = majorStart + (maj * majorStep);
            final double minUnitPosition = minorStart + (  i * minorStep);
            final double minFragPosition = Mx * minUnitPosition + Kx;
            if (Floats.leanEq (minUnitPosition, majUnitPosition)) {
                consumer.accept (minUnitPosition, minFragPosition, true, true, maj);
                consumer.accept (minUnitPosition, minFragPosition, false, true, i);
                ++maj;
            }
            else {
                consumer.accept (minUnitPosition, minFragPosition, false, false, i);
            }
        }
    }
}
