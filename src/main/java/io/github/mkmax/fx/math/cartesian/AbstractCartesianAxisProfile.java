package io.github.mkmax.fx.math.cartesian;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCartesianAxisProfile implements CartesianAxisProfile {

    public interface MajorAxisToggleListener {
        void onMajorAxisToggled ();
    }

    public interface MinorAxisToggleListener {
        void onMinorAxisToggled ();
    }

    private final List<MajorAxisToggleListener> majorAxisToggleListeners = new ArrayList<> ();
    private final List<MinorAxisToggleListener> minorAxisToggleListeners = new ArrayList<> ();

    private boolean computeMajorAxisPoints = true;
    private boolean computeMinorAxisPoints = true;

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

    /* +-----------------------------------------------------------------+ */
    /* | Enable/Disable implementation enabling event driven programming | */
    /* +-----------------------------------------------------------------+ */

    public void setComputeMajorAxisPoints (boolean enable) {
        final boolean actuallyChanged = computeMajorAxisPoints != enable;
        computeMajorAxisPoints = enable;
        if (actuallyChanged)
            majorAxisToggleListeners.forEach (MajorAxisToggleListener::onMajorAxisToggled);
    }

    public boolean wouldComputeMajorAxisPoints () {
        return computeMajorAxisPoints;
    }

    public void setComputeMinorAxisPoints (boolean enable) {
        final boolean actuallyChanged = computeMinorAxisPoints != enable;
        computeMinorAxisPoints = enable;
        if (actuallyChanged)
            minorAxisToggleListeners.forEach (MinorAxisToggleListener::onMinorAxisToggled);
    }

    public boolean wouldComputeMinorAxisPoints () {
        return computeMinorAxisPoints;
    }

}
