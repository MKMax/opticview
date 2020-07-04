package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.layout.OrthoRegion;
import io.github.mkmax.opticview.util.Disposable;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public final class GraphFrame extends OrthoRegion implements Disposable {

    private final GraphData data = new GraphData ();
    private final GraphGrid grid = new GraphGrid ();
    private final GraphView view = new GraphView (data);
    private final GraphLegend legend = new GraphLegend ();

    private final StackPane graphstack = new StackPane ();
    private final Label ylabel = new Label ();
    private final Label xlabel = new Label ();

    {
        /* do initialization here */
    }

    /* +----------------+ */
    /* | INITIALIZATION | */
    /* +----------------+ */
    public GraphFrame (
        double left,
        double right,
        double bottom,
        double top)
    {
        setWindow (left, right, bottom, top);
    }

    public GraphFrame (double uniform) {
        this (
            -uniform,
             uniform,
            -uniform,
             uniform);
    }

    public GraphFrame () {
        this (1d);
    }

    /* +-+ */
    /* | | */
}
