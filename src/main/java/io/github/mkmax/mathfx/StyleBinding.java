package io.github.mkmax.mathfx;

import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

import java.util.Objects;

public class StyleBinding {

    @SuppressWarnings ("unchecked")
    public static <T extends Styleable> void bindStyle (T of, T to) {
        Objects.requireNonNull (of);
        Objects.requireNonNull (to);
        to.getCssMetaData ().forEach (genericMeta -> {
            try {
                var boundMeta       = (CssMetaData<? super T, ?>) genericMeta;
                var parentProperty  = (StyleableProperty<Object>) boundMeta.getStyleableProperty (of);
                var bindingProperty = (StyleableProperty<Object>) boundMeta.getStyleableProperty (to);
                if (bindingProperty instanceof ObjectProperty)
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
                 * since we're not writing to those objects.
                 */
            }
        });
    }

}
