package io.github.mkmax.opticview;

import io.github.mkmax.opticview.material.Glass;
import io.github.mkmax.opticview.scene.controls.GlassListCell;
import io.github.mkmax.opticview.scene.forms.DiscreteIntervalForm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
//        ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList ();
//        XYChart.Series<Number, Number> seriesA = new XYChart.Series<> ();
//        seriesA.getData ().addAll (
//            new XYChart.Data<> (-1d, -1d),
//            new XYChart.Data<> ( 1d,  1d)
//        );
//
//        XYChart.Series<Number, Number> seriesB = new XYChart.Series<> ();
//        seriesB.getData ().addAll (
//            new XYChart.Data<> (-2d,  1d),
//            new XYChart.Data<> ( 2d, -1d)
//        );
//
//        data.addAll (seriesA, seriesB);
//
//        NumberAxis
//            x = new NumberAxis (),
//            y = new NumberAxis ();
//
//        XYChart<Number, Number> chart = new LineChart<> (x, y);
//
//        chart.setLegendVisible (false);
//        chart.setData (data);

        ComboBox<Glass> cb = new ComboBox<> ();
        cb.getItems ().addAll (Glass.values ());
        cb.setButtonCell (new GlassListCell ());
        cb.setCellFactory (view -> new GlassListCell ());

        DiscreteIntervalForm dif = new DiscreteIntervalForm (
            "λ",
            "min:",
            "step:",
            "max:");
        dif.setMaxWidth (256);
        dif.setMaxHeight (256);

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (dif, cb);

        Scene scene = new Scene (parent);
        scene.getStylesheets ().add ("style/light/main.css");

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
