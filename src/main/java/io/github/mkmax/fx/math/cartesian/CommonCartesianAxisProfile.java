package io.github.mkmax.fx.math.cartesian;

import io.github.mkmax.util.data.ArrayIterable;
import io.github.mkmax.util.math.FloatingPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonCartesianAxisProfile implements CartesianAxisProfile {

    protected static final Iterable<CartesianAxisPoint> EMPTY_CAP_ITERABLE = ArrayIterable.empty ();

    /* Major Axis Profile (MAAP) Toggle Listener */
    public interface MAAPToggleListener {
        void onMAAPToggled (boolean newState);
    }

    /* Minor Axis Profile (MIAP) Toggle Listener */
    public interface MIAPToggleListener {
        void onMIAPToggled (boolean newState);
    }

    /* Minimum Fragments Per Unit (MFPU) Change Listener */
    public interface MFPUChangeListener {
        void onMFPUChanged (double oldMfpu, double newMfpu);
    }

    private final List<MAAPToggleListener> maapToggleListeners = new ArrayList<> ();
    private final List<MIAPToggleListener> miapToggleListeners = new ArrayList<> ();
    private final List<MFPUChangeListener> mfpuChangeListeners = new ArrayList<> ();

    private boolean computeMaaps = true;
    private boolean computeMiaps = true;

    /* NOTE: perhaps later these limitations will be lifted on extending
     * classes to enable them to create their own more specific restrictions.
     */
    private double lowestMfpu;
    private double highestMfpu;
    private double mfpu;

    public CommonCartesianAxisProfile (
        double pLowestMfpu,
        double pHighestMfpu,
        double pMfpu)
    {
        lowestMfpu  = pLowestMfpu;
        highestMfpu = pHighestMfpu;
        /* The function call will ensure 'mfpu' is within bounds */
        setMinimumFragmentsPerUnit (pMfpu);
    }

    /* +-----------------------------------------------+ */
    /* | Registration/Unregistering of event listeners | */
    /* +-----------------------------------------------+ */

    public void register (MAAPToggleListener maaptl) {
        if (maaptl != null)
            maapToggleListeners.add (maaptl);
    }

    public void unregister (MAAPToggleListener maaptl) {
        if (maaptl != null)
            maapToggleListeners.remove (maaptl);
    }

    public void register (MIAPToggleListener miaptl) {
        if (miaptl != null)
            miapToggleListeners.add (miaptl);
    }

    public void unregister (MIAPToggleListener miaptl) {
        if (miaptl != null)
            miapToggleListeners.remove (miaptl);
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

    public double getLowestMinimumFragmentsPerUnit () {
        return lowestMfpu;
    }

    public double getHighestMinimumFragmentsPerUnit () {
        return highestMfpu;
    }

    public double getMinimumFragmentsPerUnit () {
        return mfpu;
    }

    public void setMinimumFragmentsPerUnit (double nMfpu) {
        nMfpu = Math.max (lowestMfpu, Math.min (highestMfpu, nMfpu));
        if (FloatingPoint.strictEq (mfpu, nMfpu))
            return;
        double old = mfpu;
        mfpu = nMfpu;
        mfpuChangeListeners.forEach (l -> l.onMFPUChanged (old, mfpu));
    }

    @Override
    public void setComputeMajorAxisPoints (boolean enable) {
        if (computeMaaps == enable)
            return;
        computeMaaps = enable;
        maapToggleListeners.forEach (l -> l.onMAAPToggled (computeMaaps));
    }

    @Override
    public boolean wouldComputeMajorAxisPoints () {
        return computeMaaps;
    }

    @Override
    public void setComputeMinorAxisPoints (boolean enable) {
        if (computeMiaps == enable)
            return;
        computeMiaps = enable;
        miapToggleListeners.forEach (l -> l.onMIAPToggled (computeMiaps));
    }

    @Override
    public boolean wouldComputeMinorAxisPoints () {
        return computeMiaps;
    }

}
