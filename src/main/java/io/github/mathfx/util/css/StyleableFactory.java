package io.github.mathfx.util.css;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.converter.SizeConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StyleableFactory<S extends Styleable> {

    private final List<CssMetaData<? extends Styleable, ?>> modCssMetaData   = new ArrayList<> ();
    private final List<CssMetaData<? extends Styleable, ?>> constCssMetaData = Collections.unmodifiableList (modCssMetaData);

    public StyleableFactory (List<CssMetaData<? extends Styleable, ?>> parentCssMetaData) {
        modCssMetaData.addAll (parentCssMetaData);
    }

    public <V> CssMetaDataBuilder<S, V> buildCssMetaData () {
        return new CssMetaDataBuilder<> (this);
    }

    public CssMetaDataBuilder<S, Number> buildSizeCssMetaData () {
        return new CssMetaDataBuilder<S, Number> (this).setConverter (SizeConverter.getInstance ());
    }

    public <V> StyleableObjectPropertyBuilder<S, V> buildStyleableProperty () {
        return new StyleableObjectPropertyBuilder<> (this);
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData () {
        return constCssMetaData;
    }

    void submit (CssMetaData<? extends Styleable, ?> nCssMetaData) {
        Objects.requireNonNull (nCssMetaData);
        if (registered (nCssMetaData))
            throw new IllegalArgumentException ("meta data with the same css property name was already registered");
        modCssMetaData.add (nCssMetaData);
    }

    CssMetaData<? extends Styleable, ?> find (String cssProperty) {
        if (cssProperty == null)
            return null;
        for (var i : constCssMetaData)
            if (i.getProperty ().equals (cssProperty)) return i;
        return null;
    }

    private boolean registered (CssMetaData<? extends Styleable, ?> cssMetaData) {
        if (cssMetaData == null)
            return false;
        for (var i : constCssMetaData)
            if (i.getProperty ().equals (cssMetaData.getProperty ())) return true;
        return false;
    }
}
