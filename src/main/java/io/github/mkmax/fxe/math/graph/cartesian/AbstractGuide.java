package io.github.mkmax.fxe.math.graph.cartesian;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import java.util.Objects;

public abstract class AbstractGuide extends Pane implements Disposable {

    /* +---------------------+ */
    /* | CONSTRUCTORS + DATA | */
    /* +---------------------+ */

    public enum Degree {
        MINOR,
        MAJOR
    }

    protected final Grid grid;
    private final ChangeListener<? super Number>            ON_WIDTH_CHANGED                  = this::onContainerWidthChanged;
    private final ChangeListener<? super Number>            ON_HEIGHT_CHANGED                 = this::onContainerHeightChanged;
    private final ChangeListener<? super Number>            ON_POSITION_CHANGED               = this::onPositionChanged;
    private final ChangeListener<? super Number>            ON_LABEL_POSITION_CHANGED         = this::onLabelPositionChanged;
    private final ChangeListener<? super Number>            ON_VALUE_CHANGED                  = this::onValueChanged;
    private final ChangeListener<? super Degree>            ON_DEGREE_CHANGED                 = this::onDegreeChanged;
    private final ChangeListener<? super Number>            ON_MAJOR_GUIDE_WIDTH_CHANGED      = this::onMajorGuideWidthChanged;
    private final ChangeListener<? super Paint>             ON_MAJOR_GUIDE_FILL_CHANGED       = this::onMajorGuideFillChanged;
    private final ChangeListener<? super Number>            ON_MINOR_GUIDE_WIDTH_CHANGED      = this::onMinorGuideWidthChanged;
    private final ChangeListener<? super Paint>             ON_MINOR_GUIDE_FILL_CHANGED       = this::onMinorGuideFillChanged;
    private final ChangeListener<? super Paint>             ON_MAJOR_LABEL_FOREGROUND_CHANGED = this::onMajorLabelForegroundChanged;
    private final ChangeListener<? super Paint>             ON_MAJOR_LABEL_BACKGROUND_CHANGED = this::onMajorLabelBackgroundChanged;
    private final ChangeListener<? super Paint>             ON_MINOR_LABEL_FOREGROUND_CHANGED = this::onMinorLabelForegroundChanged;
    private final ChangeListener<? super Paint>             ON_MINOR_LABEL_BACKGROUND_CHANGED = this::onMinorLabelBackgroundChanged;
    private final ChangeListener<? super Grid.LabelJustify> ON_VER_LABEL_JUSTIFY_CHANGED      = this::onVerLabelJustifyChanged;
    private final ChangeListener<? super Grid.LabelJustify> ON_HOR_LABEL_JUSTIFY_CHANGED      = this::onHorLabelJustifyChanged;

    private final DoubleProperty         position      = new SimpleDoubleProperty (0d);
    private final DoubleProperty         labelPosition = new SimpleDoubleProperty (0d);
    private final DoubleProperty         value         = new SimpleDoubleProperty (0d);
    private final ObjectProperty<Degree> degree        = new SimpleObjectProperty<> (Degree.MAJOR);

    AbstractGuide (Grid pView) {
        grid = Objects.requireNonNull (pView);
        grid.widthProperty                ().addListener (ON_WIDTH_CHANGED);
        grid.heightProperty               ().addListener (ON_HEIGHT_CHANGED);
        grid.majorGuideWidthProperty      ().addListener (ON_MAJOR_GUIDE_WIDTH_CHANGED);
        grid.majorGuideFillProperty       ().addListener (ON_MAJOR_GUIDE_FILL_CHANGED);
        grid.minorGuideWidthProperty      ().addListener (ON_MINOR_GUIDE_WIDTH_CHANGED);
        grid.minorGuideFillProperty       ().addListener (ON_MINOR_GUIDE_FILL_CHANGED);
        grid.majorLabelForegroundProperty ().addListener (ON_MAJOR_LABEL_FOREGROUND_CHANGED);
        grid.majorLabelBackgroundProperty ().addListener (ON_MAJOR_LABEL_BACKGROUND_CHANGED);
        grid.minorLabelForegroundProperty ().addListener (ON_MINOR_LABEL_FOREGROUND_CHANGED);
        grid.minorLabelBackgroundProperty ().addListener (ON_MINOR_LABEL_BACKGROUND_CHANGED);
        grid.verLabelJustifyProperty ()     .addListener (ON_VER_LABEL_JUSTIFY_CHANGED);
        grid.horLabelJustifyProperty ()     .addListener (ON_HOR_LABEL_JUSTIFY_CHANGED);
        position                            .addListener (ON_POSITION_CHANGED);
        labelPosition                       .addListener (ON_LABEL_POSITION_CHANGED);
        value                               .addListener (ON_VALUE_CHANGED);
        degree                              .addListener (ON_DEGREE_CHANGED);
    }

    /* +------------+ */
    /* | PROPERTIES | */
    /* +------------+ */

    /* POSITION */

    public double getPosition () {
        return position.get ();
    }

    public void setPosition (double position) {
        this.position.set (position);
    }

    public DoubleProperty positionProperty () {
        return position;
    }

    /* LABEL POSITION */

    public double getLabelPosition () {
        return labelPosition.get ();
    }

    public void setLabelPosition (double labelPosition) {
        this.labelPosition.set (labelPosition);
    }

    public DoubleProperty labelPositionProperty () {
        return labelPosition;
    }

    /* VALUE */

    public double getValue () {
        return value.get ();
    }

    public void setValue (double value) {
        this.value.set (value);
    }

    public DoubleProperty valueProperty () {
        return value;
    }

    /* DEGREE */

    public Degree getDegree () {
        return degree.get ();
    }

    public void setDegree (Degree degree) {
        this.degree.set (degree);
    }

    public ObjectProperty<Degree> degreeProperty () {
        return degree;
    }

    /* +-------------------------------------+ */
    /* | LISTENERS + DISPOSE IMPLEMENTATIONS | */
    /* +-------------------------------------+ */

    @Override
    public void dispose () {
        grid.widthProperty                ().removeListener (ON_WIDTH_CHANGED);
        grid.heightProperty               ().removeListener (ON_HEIGHT_CHANGED);
        grid.majorGuideWidthProperty      ().removeListener (ON_MAJOR_GUIDE_WIDTH_CHANGED);
        grid.majorGuideFillProperty       ().removeListener (ON_MAJOR_GUIDE_FILL_CHANGED);
        grid.minorGuideWidthProperty      ().removeListener (ON_MINOR_GUIDE_WIDTH_CHANGED);
        grid.minorGuideFillProperty       ().removeListener (ON_MINOR_GUIDE_FILL_CHANGED);
        grid.majorLabelForegroundProperty ().removeListener (ON_MAJOR_LABEL_FOREGROUND_CHANGED);
        grid.majorLabelBackgroundProperty ().removeListener (ON_MAJOR_LABEL_BACKGROUND_CHANGED);
        grid.minorLabelForegroundProperty ().removeListener (ON_MINOR_LABEL_FOREGROUND_CHANGED);
        grid.minorLabelBackgroundProperty ().removeListener (ON_MINOR_LABEL_BACKGROUND_CHANGED);
        grid.verLabelJustifyProperty ()     .removeListener (ON_VER_LABEL_JUSTIFY_CHANGED);
        grid.horLabelJustifyProperty ()     .removeListener (ON_HOR_LABEL_JUSTIFY_CHANGED);
        position                            .removeListener (ON_POSITION_CHANGED);
        labelPosition                       .removeListener (ON_LABEL_POSITION_CHANGED);
        value                               .removeListener (ON_VALUE_CHANGED);
        degree                              .removeListener (ON_DEGREE_CHANGED);
    }

    private void onContainerWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onContainerWidthChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onContainerHeightChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onContainerHeightChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onPositionChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onPositionChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onLabelPositionChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onLabelPositionChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onValueChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onValueChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onDegreeChanged (
        ObservableValue<? extends Degree> obs,
        Degree                            old,
        Degree                            now)
    {
        onDegreeChanged (old, now);
    }

    private void onMajorGuideWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onMajorGuideWidthChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onMajorGuideFillChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMajorGuideFillChanged (old, now);
    }

    private void onMinorGuideWidthChanged (
        ObservableValue<? extends Number> obs,
        Number                            old,
        Number                            now)
    {
        onMinorGuideWidthChanged (
            old.doubleValue (),
            now.doubleValue ()
        );
    }

    private void onMinorGuideFillChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMinorGuideFillChanged (old, now);
    }

    private void onMajorLabelForegroundChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMajorLabelForegroundChanged (old, now);
    }

    private void onMajorLabelBackgroundChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMajorLabelBackgroundChanged (old, now);
    }

    private void onMinorLabelForegroundChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMinorLabelForegroundChanged (old, now);
    }

    private void onMinorLabelBackgroundChanged (
        ObservableValue<? extends Paint> obs,
        Paint                            old,
        Paint                            now)
    {
        onMinorLabelBackgroundChanged (old, now);
    }

    private void onVerLabelJustifyChanged (
        ObservableValue<? extends Grid.LabelJustify> obs,
        Grid.LabelJustify                            old,
        Grid.LabelJustify                            now)
    {
        onVerLabelJustifyChanged (old, now);
    }

    private void onHorLabelJustifyChanged (
        ObservableValue<? extends Grid.LabelJustify> obs,
        Grid.LabelJustify                            old,
        Grid.LabelJustify                            now)
    {
        onHorLabelJustifyChanged (old, now);
    }

    protected void onPositionChanged (
        double oPosition,
        double nPosition)
    { /* Optional for implementations to override */ }

    protected void onLabelPositionChanged (
        double oPosition,
        double nPosition)
    { /* Optional for implementations to override */ }

    protected void onValueChanged (
        double oValue,
        double nValue)
    { /* Optional for implementations to override */ }

    protected void onDegreeChanged (
        Degree oDegree,
        Degree nDegree)
    { /* Optional for implementations to override */ }

    protected void onMajorGuideWidthChanged (
        double oWidth,
        double nWidth)
    { /* Optional for implementations to override */ }

    protected void onMajorGuideFillChanged (
        Paint oFill,
        Paint nFill)
    { /* Optional for implementations to override */ }

    protected void onMinorGuideWidthChanged (
        double oWidth,
        double nWidth)
    { /* Optional for implementations to override */ }

    protected void onMinorGuideFillChanged (
        Paint oFill,
        Paint nFill)
    { /* Optional for implementations to override */ }

    protected void onMajorLabelForegroundChanged (
        Paint oForeground,
        Paint nForeground)
    { /* Optional for implementations to override */ }

    protected void onMajorLabelBackgroundChanged (
        Paint oBackground,
        Paint nBackground)
    { /* Optional for implementations to override */ }

    protected void onMinorLabelForegroundChanged (
        Paint oForeground,
        Paint nForeground)
    { /* Optional for implementations to override */ }

    protected void onMinorLabelBackgroundChanged (
        Paint oBackground,
        Paint nForeground)
    { /* Optional for implementations to override */ }

    protected void onVerLabelJustifyChanged (
        Grid.LabelJustify oJustify,
        Grid.LabelJustify nJustify)
    { /* Optional for implementations to override */ }

    protected void onHorLabelJustifyChanged (
        Grid.LabelJustify oJustify,
        Grid.LabelJustify nJustify)
    { /* Optional for implementations to override */ }

    protected void onContainerWidthChanged (
        double oWidth,
        double nWidth)
    { /* Optional for implementations to override */ }

    protected void onContainerHeightChanged (
        double oHeight,
        double nHeight)
    { /* Optional for implementations to override */ }

    protected abstract void update ();
}
