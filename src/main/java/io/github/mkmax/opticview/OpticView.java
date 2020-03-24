package io.github.mkmax.opticview;

import io.github.mkmax.fx.math.cartesian.c2d.GraphView2D;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        GraphView2D gv = new GraphView2D ();
        gv.getAxes ().getXAxis ().setComputeMajorAxisPoints (false);
        gv.getAxes ().getYAxis ().setComputeMajorAxisPoints (false);
        gv.getTransform ().setWindow (-3, -3, +3, +3);

        BorderPane pane = new BorderPane ();
        pane.setCenter (gv);

        Scene scene = new Scene (pane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
