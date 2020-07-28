package io.github.mkmax.opticview;

import io.github.mkmax.opticview.scene.controls.DoubleField;
import io.github.mkmax.opticview.scene.forms.DiscreteIntervalForm;
import io.github.mkmax.opticview.scene.graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
        DiscreteIntervalForm dif = new DiscreteIntervalForm (
            "Lambda",
            "Lmin",
            "Lstep",
            "Lmax");

        StackPane parent = new StackPane ();
        parent.setPrefWidth (512d);
        parent.setPrefHeight (512d);
        parent.getChildren ().addAll (dif);

        Scene scene = new Scene (parent);
        scene.getStylesheets ().addAll (
            "style/doublefield.css"
        );

        stage.setScene (scene);
        stage.sizeToScene ();
        stage.centerOnScreen ();
        stage.show ();
    }
}
