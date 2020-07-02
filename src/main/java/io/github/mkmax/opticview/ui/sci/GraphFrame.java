package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.OrthoRegion;
import io.github.mkmax.opticview.util.Disposable;
import javafx.scene.control.Label;

public final class GraphFrame extends OrthoRegion implements Disposable {

    private final GraphData data = new GraphData ();
    private final GraphGrid grid = new GraphGrid ();
    private final GraphView view = new GraphView (data);
    private final GraphLegend legend = new GraphLegend ();

    private final Label ylabel = new Label ();
    private final Label xlabel = new Label ();

    {
        /* do initialization here */
    }

    public GraphFrame (
        double left,
        double right,
        double bottom,
        double top)
    {
        super (left, right, bottom, top);
    }

    public GraphFrame (double uniform) {
        super (uniform);
    }

    public GraphFrame () {
        /* already initialized */
    }




}
