package io.github.mkmax.opticview;

import io.github.mathfx.cartesian.GraphView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        StackPane content = new StackPane ();
        content.setPrefWidth (512d);
        content.setPrefHeight (512d);

        GraphView gv = new GraphView ();

        content.getChildren ().add (gv);

        Scene scene = new Scene (content);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
