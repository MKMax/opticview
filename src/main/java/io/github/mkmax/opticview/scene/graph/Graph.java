package io.github.mkmax.opticview.scene.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Region;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;

public class Graph extends Region {

    /* +-+ */
    /* | | */

    /* +--------------------------+ */
    /* | MEMBERS & INITIALIZATION | */
    /* +--------------------------+ */

    /* Function Data */
    private final GraphData data = new GraphData ();

    /* Chart Data */
    private final NumberAxis
        x = new NumberAxis (),
        y = new NumberAxis ();
    private final LineChart<Number, Number> chart = new LineChart<> (x, y);
    private final ObservableList<Series<Number, Number>> series = FXCollections.observableArrayList ();

    public Graph () {
        chart.setData (series);
    }

    /* +----------------+ */
    /* | IMPLEMENTATION | */
    /* +----------------+ */
    public GraphData getData () {
        return data;
    }
}
