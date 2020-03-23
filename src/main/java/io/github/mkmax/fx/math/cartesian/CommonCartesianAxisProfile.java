package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.FloatingPoint;

import javafx.beans.property.*;

public abstract class CommonCartesianAxisProfile implements CartesianAxisProfile {

    protected static final Iterable<CartesianAxisPoint> EMPTY_CAP_ITERABLE = ArrayIterable.empty ();

    protected static final int DECIMAL_PRECISION_MIN = 0;
    protected static final int DECIMAL_PRECISION_MAX = 16;

    private final BooleanProperty computeMaaps     = new SimpleBooleanProperty (true);
    private final BooleanProperty computeMiaps     = new SimpleBooleanProperty (true);
    private final BooleanProperty labelsEnabled    = new SimpleBooleanProperty (false);
    private final IntegerProperty decimalPrecision = new SimpleIntegerProperty (3);
    private final IntegerProperty sciNotLowerBound = new SimpleIntegerProperty (-3);
    private final IntegerProperty sciNotUpperBound = new SimpleIntegerProperty (3);

    /* NOTE: perhaps later these limitations will be lifted on extending
     * classes to enable them to create their own more specific restrictions.
     */
    private final DoubleProperty lowestMfpu  = new SimpleDoubleProperty ();
    private final DoubleProperty highestMfpu = new SimpleDoubleProperty ();
    private final DoubleProperty mfpu        = new SimpleDoubleProperty ();

    public CommonCartesianAxisProfile (
        double pLowestMfpu,
        double pHighestMfpu,
        double pMfpu)
    {
        lowestMfpu.set (pLowestMfpu);
        highestMfpu.set (pHighestMfpu);
        /* Use setter to ensure it will be within bounds */
        setMfpu (pMfpu);
    }

    /* +-----------------------------------------------------------------+ */
    /* | Enable/Disable implementation enabling event driven programming | */
    /* +-----------------------------------------------------------------+ */

    public double getLowestMfpu () {
        return lowestMfpu.get ();
    }

    public ReadOnlyDoubleProperty lowestMfpuProperty () {
        return lowestMfpu;
    }

    public double getHighestMfpu () {
        return highestMfpu.get ();
    }

    public ReadOnlyDoubleProperty highestMfpuProperty () {
        return highestMfpu;
    }

    public double getMfpu () {
        return mfpu.get ();
    }

    public void setMfpu (double nMfpu) {
        nMfpu = Math.max (lowestMfpu.get (), Math.min (highestMfpu.get (), nMfpu));
        if (!FloatingPoint.strictEq (mfpu.get (), nMfpu))
            mfpu.set (nMfpu);
    }

    public ReadOnlyDoubleProperty mfpuProperty () {
        return mfpu;
    }

    @Override
    public void setComputeMajorAxisPoints (boolean enable) {
        if (computeMaaps.get () != enable)
            computeMaaps.setValue (enable);
    }

    @Override
    public boolean shouldComputeMajorAxisPoints () {
        return computeMaaps.get ();
    }

    public ReadOnlyBooleanProperty computeMaapsProperty () {
        return computeMaaps;
    }

    @Override
    public void setComputeMinorAxisPoints (boolean enable) {
        if (computeMiaps.get () != enable)
            computeMiaps.setValue (enable);
    }

    @Override
    public boolean shouldComputeMinorAxisPoints () {
        return computeMiaps.get ();
    }

    public ReadOnlyBooleanProperty computeMiapsProperty () {
        return computeMiaps;
    }

    @Override
    public void setLabelsEnabled (boolean enabled) {
        if (labelsEnabled.get () != enabled)
            labelsEnabled.set (enabled);
    }

    @Override
    public boolean areLabelsEnabled () {
        return labelsEnabled.get ();
    }

    public ReadOnlyBooleanProperty labelsEnabledProperty () {
        return labelsEnabled;
    }

    @Override
    public void setLabelDecimalPrecision (int dp) {
        dp = Math.max (DECIMAL_PRECISION_MIN, Math.min (DECIMAL_PRECISION_MAX, dp));
        if (decimalPrecision.get () != dp)
            decimalPrecision.setValue (dp);
    }

    @Override
    public int getLabelDecimalPrecision () {
        return decimalPrecision.get ();
    }

    public ReadOnlyIntegerProperty labelDecimalPrecisionProperty () {
        return decimalPrecision;
    }

    @Override
    public void setScientificNotationLowerBound (int snlb) {
        if (sciNotLowerBound.get () != snlb)
            sciNotLowerBound.set (snlb);
    }

    @Override
    public int getScientificNotationLowerBound () {
        return sciNotLowerBound.get ();
    }

    public ReadOnlyIntegerProperty sciNotLowerBoundProperty () {
        return sciNotLowerBound;
    }

    @Override
    public void setScientificNotationUpperBound (int snhb) {
        if (sciNotUpperBound.get () != snhb)
            sciNotUpperBound.setValue (snhb);
    }

    @Override
    public int getScientificNotationUpperBound () {
        return sciNotUpperBound.get ();
    }

    public ReadOnlyIntegerProperty sciNotUpperBoundProperty () {
        return sciNotUpperBound;
    }
}
