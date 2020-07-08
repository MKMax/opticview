package io.github.mkmax.opticview;

import io.github.mkmax.opticview.ui.graph.GraphStack;

import javafx.application.Application;
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
        GraphStack gs = new GraphStack ();
        gs.getData ().createEntry (x -> 1d / x, Color.FIREBRICK, "Inverse");
        gs.getData ().createEntry (Math::sin, Color.STEELBLUE, "Sine");
        gs.getData ().createEntry (Math::cos, Color.WHEAT, "Cosine");
        gs.getData ().createEntry (Math::log, Color.SEAGREEN, "Natural Logarithm");
        gs.setWindow (-5d, +5d, -5d, +5d);

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (gs);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
