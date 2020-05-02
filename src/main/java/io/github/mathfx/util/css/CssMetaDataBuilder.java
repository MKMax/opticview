package io.github.mathfx.util.css;

import javafx.css.*;

import java.util.List;
import java.util.Objects;

public class CssMetaDataBuilder<S extends Styleable, V> {

    public interface IsSettable<A extends Styleable> {
        boolean isSettable (A styleable);
    }

    public interface GetStyleableProperty<A extends Styleable, B> {
        StyleableProperty<B> getStyleableProperty (A styleable);
    }

    /* Parameters for CssMetaData constructor */
    private String                                    property      = null;
    private StyleConverter<?, V>                      converter     = null;
    private V                                         initialValue  = null;
    private boolean                                   inherits      = false;
    private List<CssMetaData<? extends Styleable, ?>> subProperties = null;

    /* Additional parameters when implementing a custom CssMetaDataClass */
    private IsSettable<S>              isSettableImpl           = __ -> true;
    private StyleableProperty<V>       styleableProperty        = null;
    private GetStyleableProperty<S, V> getStyleablePropertyImpl = null;

    /* The factory that created this builder to submit the final CssMetaData to */
    private StyleableFactory<S> factory;

    CssMetaDataBuilder (StyleableFactory<S> pFactory) {
        factory = Objects.requireNonNull (pFactory, "factory must not be null");
    }

    public CssMetaDataBuilder<S, V> setProperty (String nProperty) {
        property = Objects.requireNonNull (nProperty, "the property must not be null");
        return this;
    }

    public CssMetaDataBuilder<S, V> setConverter (StyleConverter<?, V> nConverter) {
        converter = Objects.requireNonNull (nConverter, "the converter must not be null");
        return this;
    }

    public CssMetaDataBuilder<S, V> setInitialValue (V nValue) {
        initialValue = nValue;
        return this;
    }

    public CssMetaDataBuilder<S, V> setInherits (boolean nInherits) {
        inherits = nInherits;
        return this;
    }

    public CssMetaDataBuilder<S, V> setSubProperties (List<CssMetaData<? extends Styleable, ?>> nSubProperties) {
        subProperties = nSubProperties;
        return this;
    }

    public CssMetaDataBuilder<S, V> setIsSettableImpl (IsSettable<S> nImpl) {
        isSettableImpl = Objects.requireNonNull (nImpl, "the isSettableImpl must not be null");
        return this;
    }

    public CssMetaDataBuilder<S, V> setStyleableProperty (StyleableProperty<V> nStyleableProperty) {
        styleableProperty = nStyleableProperty;
        return this;
    }

    public CssMetaDataBuilder<S, V> setGetStyleablePropertyImpl (GetStyleableProperty<S, V> nGetStyleablePropertyImpl) {
        getStyleablePropertyImpl = nGetStyleablePropertyImpl;
        return this;
    }

    public CssMetaData<S, V> build () {
        /* Perform some provisional verifications */
        Objects.requireNonNull(property, "the property name must be specified via setProperty(String)");
        Objects.requireNonNull(converter, "the converter must be specified via setConverter(StyleConverter)");
        if (styleableProperty == null && getStyleablePropertyImpl == null)
            throw new NullPointerException (
                "either styleableProperty or getStyleablePropertyImpl must be set " +
                "via setStyleableProperty(StyleableProperty) or " +
                "setGetStyleableProperty(GetStyleableProperty) respectively");

        final CssMetaData<S, V> nCssMetaData = new CssMetaData<> (property, converter, initialValue, inherits, subProperties) {
            private final IsSettable<S> isSettableImpl =
                CssMetaDataBuilder.this.isSettableImpl;
            private final StyleableProperty<V> styleableProperty =
                CssMetaDataBuilder.this.styleableProperty;
            private final GetStyleableProperty<S, V> getStyleablePropertyImpl =
                CssMetaDataBuilder.this.getStyleablePropertyImpl;

            @Override
            public boolean isSettable (S styleable) {
                return isSettableImpl.isSettable (styleable);
            }

            @Override
            public StyleableProperty<V> getStyleableProperty (S styleable) {
                return styleableProperty == null ?
                    getStyleablePropertyImpl.getStyleableProperty (styleable) :
                    styleableProperty;
            }
        };

        factory.submit (nCssMetaData);

        return nCssMetaData;
    }
}
