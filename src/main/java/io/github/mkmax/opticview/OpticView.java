package io.github.mkmax.opticview;

import io.github.mkmax.opticview.ui.graph.GraphFrame;
import io.github.mkmax.opticview.ui.graph.GraphStack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class OpticView extends Application {

    public static void main (String... args) {
//        System.out.println (FloatUtils.toScientificString (1d, -5));
//        System.out.println (FloatUtils.toScientificString (1d, 5));
//        System.out.println (FloatUtils.toScientificString (1e-23d, 7));
//        System.out.println (FloatUtils.toScientificString (1e+23d, 12));
//        System.out.println (" --- ");
//        System.out.println (FloatUtils.toPrecisionString (1d, -5));
//        System.out.println (FloatUtils.toPrecisionString (1d, 5));
//        System.out.println (FloatUtils.toPrecisionString (1.23785022395, 7));
//        System.out.println (FloatUtils.toPrecisionString (32589.3292735, 12));
//        double totalNano = 0d;
//        for (int i = 0; i < 16_000_000; ++i) {
//            double value = Math.random ();
//            double start = System.nanoTime ();
//            Double.toString (value);
//            double end = System.nanoTime ();
//            totalNano += end - start;
//        }
//        System.out.printf ("Completed Double.toString of 16 million in %.2fms\n", totalNano * 1e-6d);
        launch (args);
    }

    @Override
    public void start (Stage stage) {
        GraphStack gs = new GraphStack ();
        gs.getGraphData ().createEntry (x -> 1d / x, Color.FIREBRICK, "Inverse");
        gs.getGraphData ().createEntry (Math::sin, Color.STEELBLUE, "Sine");
        gs.getGraphData ().createEntry (Math::cos, Color.WHEAT, "Cosine");
        gs.getGraphData ().createEntry (Math::log, Color.SEAGREEN, "Natural Logarithm");
        gs.setWindow (-5d, 5d, -5d, 5d);

        GraphFrame gf = new GraphFrame (gs);
        gf.setVerticalLabel ("Output (ms)");
        gf.setHorizontalLabel ("Input (ms)");

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (gf);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
