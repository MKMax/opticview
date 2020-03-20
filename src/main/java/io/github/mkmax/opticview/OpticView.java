package io.github.mkmax.opticview;

import io.github.mkmax.mathui.Graph;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        SimpleObjectProperty<Double> primary = new SimpleObjectProperty<> ();
        SimpleObjectProperty<Double> secondary = new SimpleObjectProperty<> ();

        primary.bind (secondary);

        primary.addListener (obs -> System.out.println ("Prim: " + obs.equals (primary)));
        secondary.addListener (obs -> System.out.println ("Seco: " + obs.equals (secondary)));

        //primary.set (5.0);
        secondary.set (5.0);

        // launch (args);
    }

    @Override
    public void start (Stage stage) {
        Graph cg = new Graph ();
        cg.setWidth (960);
        cg.setHeight (640);

        BorderPane pane = new BorderPane ();
        pane.setCenter (cg);

        Scene scene = new Scene (pane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
