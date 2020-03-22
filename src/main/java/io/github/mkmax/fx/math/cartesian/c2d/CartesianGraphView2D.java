package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.math.cartesian.AbstractCartesianAxisProfile;
import io.github.mkmax.fx.math.cartesian.AbstractCartesianAxisProfile.MajorAxisToggleListener;
import io.github.mkmax.fx.math.cartesian.AbstractCartesianAxisProfile.MinorAxisToggleListener;
import io.github.mkmax.fx.math.cartesian.AbstractCartesianAxisProfile.MFPUChangeListener;

import io.github.mkmax.fx.math.cartesian.CartesianAxisProfile;
import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.scene.canvas.GraphicsContext;

public class CartesianGraphView2D extends ResizableCanvas {

    private final CartesianGridProfile2D grid      = new CartesianGridProfile2D ();
    private final CartesianTransform2D   transform = new CartesianTransform2D ();
    private final CartesianRegistry2D    registry  = new CartesianRegistry2D ();

    private final GraphicsContext graphics = getGraphicsContext2D ();

    public CartesianGraphView2D () {
        /* Initialize before registering any listeners */
        registerAxisHandlers (grid.getXAxis ());
        registerAxisHandlers (grid.getYAxis ());

        transform.setViewport (0, getWidth (), getHeight (), 0);

        /* Register the required listeners */
        grid.registerXAxisChangeListener (this::onXYAxisProfileChanged);
        grid.registerYAxisChangeListener (this::onXYAxisProfileChanged);

        transform.register (this::onTransformRecomputed);
    }

    /* +-----------------+ */
    /* | General getters | */
    /* +-----------------+ */

    public CartesianGridProfile2D getGridProfile () {
        return grid;
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

    private void onXYAxisProfileChanged (
        CartesianAxisProfile ignore,
        CartesianAxisProfile now)
    {
        registerAxisHandlers (now);
        render ();
    }

    private void onXYMajorAxisToggled () {
        render ();
    }

    private void onXYMinorAxisToggled () {
        render ();
    }

    private void onXYMFPUChanged () {
        render ();
    }

    private void onTransformRecomputed () {
        render ();
    }

    /* +--------------------+ */
    /* | Internal functions | */
    /* +--------------------+ */

    private void registerAxisHandlers (CartesianAxisProfile on) {
        if (on instanceof AbstractCartesianAxisProfile) {
            AbstractCartesianAxisProfile acap = (AbstractCartesianAxisProfile) on;
            acap.register ((MajorAxisToggleListener) this::onXYMajorAxisToggled);
            acap.register ((MinorAxisToggleListener) this::onXYMinorAxisToggled);
            acap.register ((MFPUChangeListener)      this::onXYMFPUChanged);
        }
    }

    private void render () {

    }
}
