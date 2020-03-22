package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile.MAAPToggleListener;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile.MIAPToggleListener;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile.MFPUChangeListener;
import io.github.mkmax.fx.math.cartesian.StandardCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.c2d.CartesianTransform2D.RecomputedListener;
import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class CartesianGraphView2D extends ResizableCanvas {

    public interface AxisChangeListener {
        void onAxisChanged (
            CartesianAxisProfile old,
            CartesianAxisProfile now);
    }

    /* Predefined event handlers */
    private final AxisChangeListener XYACL    = this::onXYAxisChanged;
    private final MAAPToggleListener XYMAAPTL = this::onXYAxisMAAPToggled;
    private final MIAPToggleListener XYMIAPTL = this::onXYAxisMIAPToggled;
    private final MFPUChangeListener XYMFPUCL = this::onXYAxisMFPUChanged;

    /* Actual member data */
    private final GraphicsContext graphics = getGraphicsContext2D ();

    private final CartesianTransform2D transform = new CartesianTransform2D ();
    private final CartesianRegistry2D  registry  = new CartesianRegistry2D ();

    private final List<AxisChangeListener> xAxisChangeListeners = new ArrayList<> ();
    private final List<AxisChangeListener> yAxisChangeListeners = new ArrayList<> ();

    private CartesianAxisProfile xAxis;
    private CartesianAxisProfile yAxis;

    public CartesianGraphView2D () {
        /* Grid/Axes event handler registration */
        setXAxis (new StandardCartesianAxisProfile ());
        setYAxis (new StandardCartesianAxisProfile ());

        registerXAxisChangeListener (this::onXYAxisChanged);
        registerYAxisChangeListener (this::onXYAxisChanged);

        /* Transform event handler registration */
        transform.register (this::onTransformRecomputed);

        /* Canvas event handler registration */
        widthProperty ().addListener (this::onWidthChanged);
        heightProperty ().addListener (this::onHeightChanged);
    }

    /* +-------------------------------------+ */
    /* | Registration/Unregistering utilities | */
    /* +-------------------------------------+ */

    public void registerXAxisChangeListener (AxisChangeListener xacl) {
        if (xacl != null)
            xAxisChangeListeners.add (xacl);
    }

    public void unregisterXAxisChangeListener (AxisChangeListener xacl) {
        if (xacl != null)
            xAxisChangeListeners.remove (xacl);
    }

    public void registerYAxisChangeListener (AxisChangeListener yacl) {
        if (yacl != null)
            yAxisChangeListeners.add (yacl);
    }

    public void unregisterYAxisChangeListener (AxisChangeListener yacl) {
        if (yacl != null)
            yAxisChangeListeners.remove (yacl);
    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianAxisProfile getXAxis () {
        return xAxis;
    }

    public void setXAxis (CartesianAxisProfile nXAxis) {
        if (nXAxis == null || nXAxis == xAxis)
            return;
        CartesianAxisProfile old = xAxis;
        xAxis = nXAxis;
        xAxisChangeListeners.forEach (l -> l.onAxisChanged (old, xAxis));
    }

    public CartesianAxisProfile getYAxis () {
        return yAxis;
    }

    public void setYAxis (CartesianAxisProfile nYAxis) {
        if (nYAxis == null || nYAxis == yAxis)
            return;
        CartesianAxisProfile old = yAxis;
        yAxis = nYAxis;
        yAxisChangeListeners.forEach (l -> l.onAxisChanged (old, yAxis));
    }

    public CartesianTransform2D getTransform () {
        return transform;
    }

    public CartesianRegistry2D getRegistry () {
        return registry;
    }

    /* +----------------+ */
    /* | Event handlers | */
    /* +----------------+ */

    private void onXYAxisChanged (
        CartesianAxisProfile old,
        CartesianAxisProfile now)
    {
        if (old instanceof CommonCartesianAxisProfile) {
            CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) old;
            ccap.unregister (XYMAAPTL);
            ccap.unregister (XYMIAPTL);
            ccap.unregister (XYMFPUCL);
        }
        if (now instanceof CommonCartesianAxisProfile) {
            CommonCartesianAxisProfile ccap = (CommonCartesianAxisProfile) now;
            ccap.register (XYMAAPTL);
            ccap.register (XYMIAPTL);
            ccap.register (XYMFPUCL);
        }
        render ();
    }

    private void onXYAxisMAAPToggled (boolean stateNow) {
        render ();
    }

    private void onXYAxisMIAPToggled (boolean stateNow) {
        render ();
    }

    private void onXYAxisMFPUChanged (double oldMfpu, double nowMfpu) {
        render ();
    }

    private void onTransformRecomputed () {
        render ();
    }

    private void onWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        transform.setViewport (getWidth (), getHeight ());
        render ();
    }

    private void onHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        transform.setViewport (getWidth (), getHeight ());
        render ();
    }

    /* +--------------------+ */
    /* | Internal functions | */
    /* +--------------------+ */

    private void render () {

    }
}
