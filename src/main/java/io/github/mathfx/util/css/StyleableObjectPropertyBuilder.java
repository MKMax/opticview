package io.github.mathfx.util.css;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;

import java.util.Objects;

public class StyleableObjectPropertyBuilder<S extends Styleable, V> {

    private S      bean;
    private String name;
    private String property;
    private V      initialValue;

    private StyleableFactory<?> factory;

    StyleableObjectPropertyBuilder (StyleableFactory<?> pFactory) {
        factory = Objects.requireNonNull (pFactory);
    }

    public StyleableObjectPropertyBuilder<S, V> setBean (S nBean) {
        bean = Objects.requireNonNull (nBean, "the bean must not be null");
        return this;
    }

    public StyleableObjectPropertyBuilder<S, V> setName (String nName) {
        name = Objects.requireNonNull (nName, "the name must not be null");
        return this;
    }

    public StyleableObjectPropertyBuilder<S, V> setProperty (String nProperty) {
        property = Objects.requireNonNull (nProperty, "the css property name must not be null");
        return this;
    }

    public StyleableObjectPropertyBuilder<S, V> setInitialValue (V nValue) {
        initialValue = nValue;
        return this;
    }

    @SuppressWarnings ("unchecked")
    public StyleableObjectProperty<V> build () {
        /* Perform some provisional verifications */
        Objects.requireNonNull (bean, "the bean object must be specified via setBean(Object)");
        Objects.requireNonNull (name, "the name must be specified via setName(String)");
        Objects.requireNonNull (property, "the property must be specified via setProperty(String)");

        final CssMetaData<S, V> md;
        try {
            md = (CssMetaData<S, V>) factory.find (property);
            if (md == null)
                throw new IllegalStateException ("css meta data must be registered prior to creation of a property");
        }
        catch (ClassCastException e) {
            throw new IllegalStateException ("css meta data type mismatch");
        }
        final V rInitialValue = initialValue == null ? md.getInitialValue (bean) : initialValue;

        return new StyleableObjectProperty<> (rInitialValue) {
            private final Object bean = StyleableObjectPropertyBuilder.this.bean;
            private final String name = StyleableObjectPropertyBuilder.this.name;
            private final CssMetaData<? extends Styleable, V> cssMetaData = md;

            @Override
            public Object getBean () {
                return this.bean;
            }

            @Override
            public String getName () {
                return this.name;
            }

            @Override
            public CssMetaData<? extends Styleable, V> getCssMetaData () {
                return this.cssMetaData;
            }
        };
    }
}
