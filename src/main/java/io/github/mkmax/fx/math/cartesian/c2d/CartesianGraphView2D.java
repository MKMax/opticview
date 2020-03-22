package io.github.mkmax.fx.math.cartesian.c2d;

import io.github.mkmax.fx.util.ResizableCanvas;

import javafx.scene.canvas.GraphicsContext;

public class CartesianGraphView2D extends ResizableCanvas {

    private final CartesianGridProfile2D grid      = new CartesianGridProfile2D ();
    private final CartesianTransform2D   transform = new CartesianTransform2D ();
    private final CartesianRegistry2D    registry  = new CartesianRegistry2D ();

    private final GraphicsContext graphics = getGraphicsContext2D ();

    public CartesianGraphView2D () {

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


    /* +--------------------+ */
    /* | Internal functions | */
    /* +--------------------+ */

    private void render () {

    }
}
