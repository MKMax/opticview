package io.github.mkmax.fx;

import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

import java.util.List;
import java.util.Objects;

public class StyleBinding {

    /**
     * Given two components of the same type, this method will attempt to
     * bind as many {@link StyleableProperty} objects provided by
     * {@link Styleable#getCssMetaData()} as possible.
     * <p>
     * Some properties may not be bound mainly due to the fact that
     * {@code T} is a supertype of a sub-component whose
     * {@link CssMetaData#getStyleableProperty(Styleable)} requires
     * a styleable of a more specific type than provided. In this case,
     * this method does not bind such properties and continues trying
     * to bind others.
     *
     * @param of The {@link Styleable} whose properties are to be bound.
     * @param to The binding target for {@code of}.
     * @param <T> The generic {@link Styleable} type.
     */
    @SuppressWarnings ("unchecked")
    public static <T extends Styleable> void bindStyle (T of, T to) {
        Objects.requireNonNull (of);
        Objects.requireNonNull (to);
        to.getCssMetaData ().forEach (genericMeta -> {
            try {
                var boundMeta       = (CssMetaData<? super T, ?>) genericMeta;
                var bindingProperty = (StyleableProperty<Object>) boundMeta.getStyleableProperty (of);
                var parentProperty  = (StyleableProperty<Object>) boundMeta.getStyleableProperty (to);
                if (bindingProperty instanceof ObjectProperty)
                    /* the instanceof check above guarantees the same for 'parentProperty' */
                    ((ObjectProperty<Object>) bindingProperty).bind ((ObjectProperty<Object>) parentProperty);
            }
            catch (ClassCastException ignore) {
                /* If an exception is thrown here, this indicates that T itself
                 * is a superclass of another component. This means that there
                 * is nothing we can do to retrieve the styleable property since
                 * we cannot infer the type of the specific subclass. Clients
                 * should be aware of this, and if they wish to FULLY bind the
                 * style, they must specify a base instance that is NOT a superclass
                 * of another component. That being said, this method will
                 * continue to try and bind as many styleable properties as possible.
                 *
                 * This exception will never occur from the StyleableProperty<Object> cast
                 * since we're not writing (or reading for that matter) to those objects.
                 */
            }
        });
    }

}
