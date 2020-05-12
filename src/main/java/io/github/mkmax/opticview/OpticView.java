package io.github.mkmax.opticview;

import io.github.mkmax.mathfx.StyleBinding;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
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

        Label a = new Label ("Label #1");
        Label b = new Label ("Label #2");
        StackPane.setAlignment (a, Pos.TOP_LEFT);
        StackPane.setAlignment (b, Pos.BOTTOM_RIGHT);

        a.fontProperty ().set (Font.font ("Times New Roman", 48));
        a.paddingProperty ().set (new Insets (15d));

        StyleBinding.bindStyle (a, b);

        parent.getChildren ().addAll (a, b);

        Scene scene = new Scene (parent);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
