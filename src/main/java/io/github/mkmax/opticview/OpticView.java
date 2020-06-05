package io.github.mkmax.opticview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) {
        HorizontalAxis haxis = new HorizontalAxis ();

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (haxis);

        Scene scene = new Scene (parent);

        stage.setOnShown (e -> {
            System.out.println (haxis.map (0));
        });

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
