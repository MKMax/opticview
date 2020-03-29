package io.github.mkmax.opticview;

import io.github.mkmax.fxe.math.graph.cartesian.GraphView2a;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        Pane pane = FXMLLoader.load (getClass ().getClassLoader ().getResource ("test.fxml"));

        GraphView2a gv = (GraphView2a) pane.getChildren ().get (0);
        Button btn = (Button) pane.getChildren ().get (1);

        gv.setOnMouseClicked (e -> {
            System.out.println (gv.getOpacity ());
        });

        Scene scene = new Scene (pane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
