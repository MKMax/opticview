package io.github.mkmax.opticview;

import io.github.mkmax.opticview.scene.AppPane;
import io.github.mkmax.opticview.scene.AuthorPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

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
        final InputStream iconSource =
            Thread.currentThread ().getContextClassLoader ().getResourceAsStream ("icons/ico512.png");
        if (iconSource == null) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle ("Startup Failure");
            alert.setHeaderText ("Startup Failure");
            alert.setContentText ("Failed to open application icon resource icons/ico512.png");
            alert.showAndWait ();
            return;
        }

        stage.setTitle ("OpticView Revision 1.1");
        stage.getIcons ().add (
            new Image (iconSource));

        BorderPane root = new BorderPane ();
        root.setPrefWidth (960d);
        root.setPrefHeight (640d);
        root.setCenter (new AppPane ());
        root.setBottom (new AuthorPane (
            "OpticView Revision 1.1 © Maxim Kasyanenko. Licensed Under GNU GPLv2",
            "https://mkmax.github.io",
            "https://github.com/mkmax/"));

        Scene scene = new Scene (root);
        scene.getStylesheets ().add ("style/light/main.css");

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
