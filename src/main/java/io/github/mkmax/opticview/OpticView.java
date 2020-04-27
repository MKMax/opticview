package io.github.mkmax.opticview;

import io.github.mathfx.cartesian.GraphView;
import io.github.mathfx.cartesian.HorizontalGuide;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader (getClass ().getClassLoader ().getResource ("main.fxml"));

        StackPane stackPane = loader.load ();
        Pane pane = (Pane) stackPane.getChildren ().get (0);
        HorizontalGuide hg = (HorizontalGuide) pane.getChildren ().get (0);

        Scene scene = new Scene (stackPane);

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
