module io.github.mkmax.opticview {
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    opens io.github.mkmax.opticview to
        javafx.base,
        javafx.controls,
        javafx.graphics;
    requires org.controlsfx.controls;
    requires org.joml;
}