package io.github.mkmax.opticview;

import io.github.mkmax.fxe.math.graph.cartesian.HorizontalGuide;
import io.github.mkmax.fxe.math.graph.cartesian.LabeledRectangleGuide;
import io.github.mkmax.fxe.math.graph.cartesian.TextGuide;
import io.github.mkmax.fxe.math.graph.cartesian.VerticalGuide;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.text.Font;
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

        LabeledRectangleGuide g = new HorizontalGuide ();
        TextGuide.Appearance ga = g.getAppearance ();
        ga.setTextFont (Font.font ("Consolas", 24));
        ga.setTextPadding (5d);
        ga.setSize (2.5d);
        g.setText ("Hello, world!");
        g.setJustify (TextGuide.Justify.CENTER);
        g.setOrtho (256 + 128);
        g.setParallel (256 + 128);

        LabeledRectangleGuide h = new VerticalGuide ();
        TextGuide.Appearance ha = h.getAppearance ();
        ha.setTextFont (Font.font ("Consolas", 24));
        ha.setTextPadding (5d);
        ha.setSize (2.5d);
        h.setText ("Another, world!");
        h.setJustify (TextGuide.Justify.CENTER);
        h.setOrtho (256 - 128);
        h.setParallel (256 - 128);

        pane.getChildren ().addAll (g, h);

        Scene scene = new Scene (pane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
