package io.github.mkmax.opticview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpticView extends Application {

    public static void main (String... args) {
        //launch (args);
    }

    @Override
    public void start (Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader (getClass ().getClassLoader ().getResource ("main.fxml"));

        Scene scene = new Scene (loader.load ());

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
