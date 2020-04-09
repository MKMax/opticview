package io.github.mkmax.opticview;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
//        Pane pane = FXMLLoader.load (getClass ().getClassLoader ().getResource ("test.fxml"));
//
//        GraphView2d gv = (GraphView2d) pane.getChildren ().get (0);
//        Button btn = (Button) pane.getChildren ().get (1);
//
//        gv.setOnMouseClicked (e -> {
//            System.out.println (gv.getOpacity ());
//        });

        Pane cPane = new Pane ();
        cPane.setPrefWidth (512d);
        cPane.setPrefHeight (512d);

        Pane hPane = new Pane ();
        hPane.setMaxWidth (0d);
        hPane.setMaxHeight (0d);
        hPane.setLayoutX (  0d);
        hPane.setLayoutY (128d);

        Rectangle hGuide = new Rectangle ();
        hGuide.setFill (Color.LIGHTGRAY);
        hGuide.setWidth (512d);
        hGuide.setHeight (16d);
        hGuide.setLayoutX (0d);
        hGuide.setLayoutY (-8d);

        Rectangle iGuide = new Rectangle ();
        iGuide.setFill (Color.RED);
        iGuide.setWidth (512d);
        iGuide.setHeight (8d);
        iGuide.setLayoutX (0d);
        iGuide.setLayoutY (-4d);

        hPane.getChildren ().addAll (hGuide, iGuide);
        cPane.getChildren ().addAll (hPane);

        Scene scene = new Scene (cPane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
