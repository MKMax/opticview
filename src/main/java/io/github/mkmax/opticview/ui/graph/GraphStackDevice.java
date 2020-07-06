package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.ui.layout.OrthoRegion;
import io.github.mkmax.opticview.util.Disposable;

import java.util.Objects;

/* Since all objects part of the GraphStack are OrthoRegions requiring a GraphData,
 * this class shall serve the purpose of being that region with a pre-implemented
 * GraphData container and management.
 */
public abstract class GraphStackDevice extends OrthoRegion implements Disposable {

    /* +------------+ */
    /* | GRAPH DATA | */
    /* +------------+ */
    protected GraphData graphData;

    public GraphData getGraphData () {
        return graphData;
    }

    public void setGraphData (GraphData ndata) {
        Objects.requireNonNull (ndata, "A non-null GraphData object must be specified");
        onGraphDataChanged (graphData, ndata);
        graphData = ndata;
    }

    /* +----------+ */
    /* | HANDLERS | */
    /* +----------+ */
    protected abstract void onGraphDataChanged (GraphData old, GraphData now);
}
