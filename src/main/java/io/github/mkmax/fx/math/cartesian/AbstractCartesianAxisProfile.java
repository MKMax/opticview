package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.FloatingPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCartesianAxisProfile implements CartesianAxisProfile {

    public interface MajorAxisToggleListener {
        void onMajorAxisToggled ();
    }

    public interface MinorAxisToggleListener {
        void onMinorAxisToggled ();
    }

    public interface MFPUChangeListener {
        void onMFPUChanged ();
    }

    protected static final Iterable<CartesianAxisPoint> EMPTY = ArrayIterable.empty ();

    private static final double MIN_MFPU = 1d;

    private final List<MajorAxisToggleListener> majorAxisToggleListeners = new ArrayList<> ();
    private final List<MinorAxisToggleListener> minorAxisToggleListeners = new ArrayList<> ();
    private final List<MFPUChangeListener>      mfpuChangeListeners      = new ArrayList<> ();

    private boolean computeMajorAxisPoints = true;
    private boolean computeMinorAxisPoints = true;

    private double mfpu = 128d;

    public AbstractCartesianAxisProfile (double pMfpu) {
        setMinimumFragmentsPerUnit (pMfpu);
    }

    public AbstractCartesianAxisProfile () {
        /* Initial state already achieved */
    }

    /* +-----------------------------------------------+ */
    /* | Registration/Unregistering of event listeners | */
    /* +-----------------------------------------------+ */

    public void register (MajorAxisToggleListener matl) {
        if (matl != null)
            majorAxisToggleListeners.add (matl);
    }

    public void unregister (MajorAxisToggleListener matl) {
        if (matl != null)
            majorAxisToggleListeners.remove (matl);
    }

    public void register (MinorAxisToggleListener matl) {
        if (matl != null)
            minorAxisToggleListeners.add (matl);
    }

    public void unregister (MinorAxisToggleListener matl) {
        if (matl != null)
            minorAxisToggleListeners.remove (matl);
    }

    public void register (MFPUChangeListener mfpucl) {
        if (mfpucl != null)
            mfpuChangeListeners.add (mfpucl);
    }

    public void unregister (MFPUChangeListener mfpucl) {
        if (mfpucl != null)
            mfpuChangeListeners.remove (mfpucl);
    }

    /* +-----------------------------------------------------------------+ */
    /* | Enable/Disable implementation enabling event driven programming | */
    /* +-----------------------------------------------------------------+ */

    @Override
    public void setMinimumFragmentsPerUnit (double nMfpu) {
        nMfpu = Math.max (MIN_MFPU, nMfpu);
        if (FloatingPoint.equal (mfpu, nMfpu))
            return;
        mfpu = nMfpu;
        mfpuChangeListeners.forEach (MFPUChangeListener::onMFPUChanged);
    }

    @Override
    public double getMinimumFragmentsPerUnit () {
        return mfpu;
    }

    @Override
    public void setComputeMajorAxisPoints (boolean enable) {
        if (computeMajorAxisPoints == enable)
            return;
        computeMajorAxisPoints = enable;
        majorAxisToggleListeners.forEach (MajorAxisToggleListener::onMajorAxisToggled);
    }

    @Override
    public boolean wouldComputeMajorAxisPoints () {
        return computeMajorAxisPoints;
    }

    @Override
    public void setComputeMinorAxisPoints (boolean enable) {
        if (computeMinorAxisPoints == enable)
            return;
        computeMinorAxisPoints = enable;
        minorAxisToggleListeners.forEach (MinorAxisToggleListener::onMinorAxisToggled);
    }

    @Override
    public boolean wouldComputeMinorAxisPoints () {
        return computeMinorAxisPoints;
    }

}
