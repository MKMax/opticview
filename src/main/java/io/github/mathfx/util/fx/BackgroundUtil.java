package io.github.mathfx.util.fx;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class BackgroundUtil {

    public static Background newFillBackground (Color color) {
        return new Background (new BackgroundFill (color, null, null));
    }

}
