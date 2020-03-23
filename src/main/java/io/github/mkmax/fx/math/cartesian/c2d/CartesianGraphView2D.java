package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.CommonCartesianAxisProfile.*;
import io.github.mkmax.fx.math.cartesian.StandardCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.c2d.CartesianTransform2D.*;
import io.github.mkmax.fx.math.cartesian.c2d.CartesianAxes2D.*;
import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;

public class CartesianGraphView2D extends ResizableCanvas {

    /* Predefined event handlers */
    private final XAxisChangeListener XACL     = this::onXYAxisChanged;
    private final YAxisChangeListener YACL     = this::onXYAxisChanged;
    private final MAAPToggleListener  XYMAAPTL = this::onXYAxisMAAPToggled;
    private final MIAPToggleListener  XYMIAPTL = this::onXYAxisMIAPToggled;
    private final MFPUChangeListener  XYMFPUCL = this::onXYAxisMFPUChanged;
    private final RecomputedListener  TRCL     = this::onTransformRecomputed;

    /* Actual member data */
    private final GraphicsContext graphics = getGraphicsContext2D ();

    private final CartesianTransform2D transform;
    private final CartesianRegistry2D registry;
    private final CartesianAxes2D axes;

    public CartesianGraphView2D () {

    }

    /* +---------------------------+ */
    /* | General getters & setters | */
    /* +---------------------------+ */

    public CartesianTransform2D getTransform () {
        return transform;
    }

    public CartesianRegistry2D getRegistry () {
        return registry;
    }

    public CartesianAxes2D getAxes () {
        return axes;
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
