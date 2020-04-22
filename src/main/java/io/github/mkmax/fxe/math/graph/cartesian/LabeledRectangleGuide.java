package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class LabeledRectangleGuide extends TextGuide {

    private final ChangeListener<? super Number> LABEL_DIM = this::labelDim;

    protected final TextGuide.Appearance appearance = new TextGuide.Appearance ();
    protected final Rectangle            guide      = new Rectangle ();
    protected final Label                label      = new Label ();

    public LabeledRectangleGuide () {
        guide.setFill (appearance.getColor ());
        label.setTextFill (appearance.getTextForeground ());
        setLabelBackground (appearance.getTextBackground ());

        label.widthProperty  ().addListener (LABEL_DIM);
        label.heightProperty ().addListener (LABEL_DIM);

        getChildren ().addAll (guide, label);
    }

    @Override
    @SuppressWarnings ("unchecked")
    public <T extends Guide.Appearance> T getAppearance () {
        return (T) appearance;
    }

    @Override
    public void dispose () {
        super.dispose ();
        label.widthProperty  ().removeListener (LABEL_DIM);
        label.heightProperty ().removeListener (LABEL_DIM);
    }

    /* +-----------+ */
    /* | LISTENERS | */
    /* +-----------+ */

    @Override
    protected void onParentWidthChanged (double old, double now) {
        repositionSelf ();
    }

    @Override
    protected void onParentHeightChanged (double old, double now) {
        repositionSelf ();
    }

    @Override
    protected void onOrthoChanged (double old, double now) {
        repositionSelf ();
    }

    @Override
    protected void onParallelChanged (double old, double now) {
        repositionSelf ();
    }

    @Override
    protected void onVisibilityChanged (boolean old, boolean now) {
        setVisible (now);
    }

    @Override
    protected void onColorChanged (Color old, Color now) {
        guide.setFill (now);
    }

    @Override
    protected void onTextChanged (String old, String now) {
        label.setText (now);
        repositionLabel ();
    }

    @Override
    protected void onTextJustifyChanged (Justify old, Justify now) {
        repositionLabel ();
    }

    @Override
    protected void onTextPaddingChanged (double old, double now) {
        label.setPadding (new Insets (now));
        repositionLabel ();
    }

    @Override
    protected void onTextFontChanged (Font old, Font now) {
        label.setFont (now);
        repositionLabel ();
    }

    @Override
    public void onTextForegroundChanged (Color old, Color now) {
        label.setTextFill (now);
    }

    @Override
    public void onTextBackgroundChanged (Color old, Color now) {
        setLabelBackground (now);
    }

    private void labelDim (
        ObservableValue<? extends Number> __obs,
        Number                              old,
        Number                              now)
    {
        repositionLabel ();
    }

    /* +--------------------+ */
    /* | OPTIONAL CALLBACKS | */
    /* +--------------------+ */

    protected abstract void repositionSelf ();

    protected abstract void repositionLabel ();

    protected abstract void repositionGuide ();

    /* +---------+ */
    /* | UTILITY | */
    /* +---------+ */

    protected void setLabelBackground (Color nBackground) {
        label.setBackground (new Background (new BackgroundFill (nBackground, null, null)));
    }
}
