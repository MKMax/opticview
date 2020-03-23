package io.github.mkmax.opticview;

import io.github.mkmax.fx.math.cartesian.c2d.CartesianGraphView2D;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        CartesianGraphView2D gv = new CartesianGraphView2D ();
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
