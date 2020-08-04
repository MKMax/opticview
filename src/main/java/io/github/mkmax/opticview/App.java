package io.github.mkmax.opticview;

import io.github.mkmax.opticview.scene.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    /* +------+ */
    /* | MAIN | */
    /* +------+ */
    public static void main (String... args) {
        launch (args);
    }

    /* +-----+ */
    /* | APP | */
    /* +-----+ */
    @Override
    public void start (Stage stage) {
        MainPane mainPane = new MainPane ();

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().add (mainPane);

        Scene scene = new Scene (parent);
        scene.getStylesheets ().add ("style/light/main.css");

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
