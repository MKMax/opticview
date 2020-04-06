package io.github.mkmax.opticview;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class OpticView extends Application {

    public static void main (String... args) {
        DecimalFormat df = new DecimalFormat ("0");
        df.setRoundingMode (RoundingMode.HALF_UP);
        System.out.println (df.format (99999.5394d));

        //launch (args);
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

        StackPane sp   = new StackPane ();
        Pane      grid = new Pane ();
        grid.setMinWidth (540);
        grid.setMinHeight (460);

        Rectangle gline = new Rectangle ();
        gline.setX (10);
        gline.setY (0);
        gline.setWidth (2);
        gline.setHeight (grid.getMinHeight ());
        gline.setFill (Color.LIGHTGRAY);

        grid.getChildren ().addAll (gline);
        sp.getChildren ().addAll (grid);
        Scene scene = new Scene (sp);

        stage.setOnShown (event -> {

        });

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
