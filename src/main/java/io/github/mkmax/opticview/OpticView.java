package io.github.mkmax.opticview;

import io.github.mkmax.fx.HorizontalMappedPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) {
        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);

        HorizontalMappedPane hor = new HorizontalMappedPane ();
        hor.setOnMouseMoved (e -> System.out.printf ("in arbitrary region: %f\n", hor.unmap (e.getX ())));

        parent.getChildren ().add (hor);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
