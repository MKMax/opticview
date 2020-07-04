package io.github.mkmax.opticview;

import io.github.mkmax.opticview.ui.sci.GraphData;
import io.github.mkmax.opticview.ui.sci.GraphGrid;
import io.github.mkmax.opticview.ui.sci.GraphView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) {
        GraphData data = new GraphData ();
        data.createEntry (x -> x * x, Color.FIREBRICK);
        data.createEntry (Math::sin, Color.WHEAT);
        data.createEntry (Math::cos, Color.STEELBLUE);
        data.createEntry (Math::log, Color.SEAGREEN);

        GraphView view = new GraphView (data);
        view.setWindow (-5d, +5d, -5d, +5d);

        GraphGrid grid = new GraphGrid ();
        grid.bindOrtho (view);

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (grid, view);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
