package io.github.mkmax.opticview.ui.graph;

import io.github.mkmax.opticview.ui.layout.OrthoRegion;
import io.github.mkmax.opticview.ui.layout.OrthoStackPane;
import io.github.mkmax.opticview.util.Disposable;

import java.util.Objects;

public class GraphStack extends OrthoStackPane implements Disposable {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                        DEVICE                                             //
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static abstract class Device extends OrthoRegion implements Disposable {
        /* +---------------------+ */
        /* | GRAPH DATA PROPERTY | */
        /* +---------------------+ */
        private GraphData graphdata;

        public GraphData getGraphData () {
            return graphdata;
        }

        public void setGraphData (GraphData ndata) {
            GraphData old = graphdata;
            graphdata = Objects.requireNonNull (ndata, "A non-null GraphData object must be specified");
            onGraphDataChanged (old, graphdata);
        }

        /* +----------------------+ */
        /* | HANDLER DECLARATIONS | */
        /* +----------------------+ */
        protected abstract void onGraphDataChanged (GraphData old, GraphData now);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //                                     GRAPH STACK                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /* +------------+ */
    /* | GRAPH DATA | */
    /* +------------+ */
    private GraphData dataref;

    public GraphData getGraphData () {
        return dataref;
    }

    public void setGraphData (GraphData ndata) {
        Objects.requireNonNull (ndata, "A data object reference must be specified");
        view.setGraphData (ndata);
        legend.setGraphData (ndata);
    }

    /* +--------------------------------+ */
    /* | INITIALIZATION & OTHER MEMBERS | */
    /* +--------------------------------+ */

    /* CORE STACK COMPONENTS (from back to front in viewing order) */
    private final GraphGrid grid;
    private final GraphView view;
    private final GraphLegend legend;

    public GraphStack (GraphData ref) {
        dataref = Objects.requireNonNull (ref, "A data object reference must be specified");
        /* unfortunately we have to create the components in the ugly constructor */
        grid = new GraphGrid ();
        view = new GraphView (dataref);
        legend = new GraphLegend (dataref);
        /* binding & general component setup */
        grid.bindOrtho (this);
        view.bindOrtho (this);
        legend.bindOrtho (this);
        /* add them onto the stack component */
        getChildren ().addAll (grid, view, legend);
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
        grid.dispose ();
        view.dispose ();
        legend.dispose ();
    }
}
