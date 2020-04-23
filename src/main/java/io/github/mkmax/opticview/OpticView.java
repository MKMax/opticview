package io.github.mkmax.opticview;

import io.github.mathfx.cartesian.GraphView;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) {
        Pane pane = new Pane ();
        pane.setPrefWidth (512);
        pane.setPrefHeight (512);

        GraphView gv = new GraphView ();
        gv.setStyle ("-mfx-major-axis-foreground: red;");

        gv.majorGuideForegroundProperty ().addListener ((obs, old, now) -> System.out.println (now));

        gv.setPrefWidth (256);
        gv.setPrefHeight (256);

        pane.getChildren ().add (gv);
        Scene scene = new Scene (pane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
