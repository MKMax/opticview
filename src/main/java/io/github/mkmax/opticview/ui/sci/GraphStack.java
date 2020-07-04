package io.github.mkmax.opticview.ui.sci;

import io.github.mkmax.opticview.ui.layout.OrthoStackPane;
import io.github.mkmax.opticview.util.Disposable;

import java.util.Objects;

public class GraphStack extends OrthoStackPane implements Disposable {

    /* +------------+ */
    /* | GRAPH DATA | */
    /* +------------+ */
    private GraphData dataref;

    public GraphData getData () {
        return dataref;
    }

    public void setData (GraphData ndata) {
        Objects.requireNonNull (ndata, "A data object reference must be specified");
        view.setData (ndata);
        legend.setData (ndata);
    }

    /* +--------------------------------+ */
    /* | INITIALIZATION & OTHER MEMBERS | */
    /* +--------------------------------+ */
    private final GraphGrid grid;
    private final GraphView view;
    private final GraphLegend legend;

    public GraphStack (GraphData ref) {
        Objects.requireNonNull (ref, "A data object reference must be specified");
        grid = new GraphGrid ();
        view = new GraphView (ref);
        legend = new GraphLegend (ref);
        dataref = ref;
    }

    public GraphStack () {
        this (new GraphData ());
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    @Override
    public void dispose () {
        super.dispose ();
    }
}
