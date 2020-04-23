package io.github.mathfx.util.css;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

import java.util.List;
import java.util.function.Function;

public class QuickCssMetaData {

    public static <S extends Styleable> CssMetaData<S, Number> createNumericCssMetaData (
        final String                                 cssProperty,
        final Function<S, StyleableProperty<Number>> function,
        final Number                                 initialValue)
    {
        return createCssMetaData (cssProperty, NumberConverter.getInstance (), function, initialValue, false, null);
    }

    private static <S extends Styleable, V> CssMetaData<S, V> createCssMetaData (
        final String cssProperty,
        final StyleConverter<?, V> converter,
        final Function<S, StyleableProperty<V>> function,
        final V initialValue,
        final boolean inherit,
        final List<CssMetaData<? extends Styleable, ?>> subProperties)
    {
        if (cssProperty == null)
            throw new IllegalArgumentException ("cssProperty must not be null");
        String trimmedCssProperty = cssProperty.trim ();
        if (trimmedCssProperty.length () == 0)
            throw new IllegalArgumentException ("cssProperty must contain some non-whitespace content");

        if (converter == null)
            throw new IllegalArgumentException ("A converter must be specified");

        if (function == null)
            throw new IllegalArgumentException ("A property getter function must be specified");

        return new CssMetaData<> (cssProperty, converter, initialValue, inherit, subProperties) {
            @Override public boolean isSettable (S __) { return true; }
            @Override public StyleableProperty<V> getStyleableProperty (S i) { return function.apply (i); }
        };
    }
}
