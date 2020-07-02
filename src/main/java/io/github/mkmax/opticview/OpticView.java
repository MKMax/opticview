package io.github.mkmax.opticview;

import io.github.mkmax.opticview.ui.sci.GraphGrid;

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
        GraphGrid grid = new GraphGrid ();
        grid.setLeft (5d);
        grid.setRight (10d);
        grid.setBottom (5d);
        grid.setTop (10d);

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (grid);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
