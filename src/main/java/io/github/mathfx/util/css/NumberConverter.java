package io.github.mathfx.util.css;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public class NumberConverter extends StyleConverter<String, Number> {

    private static final class Holder {
        static final NumberConverter INSTANCE = new NumberConverter ();
    }

    public static NumberConverter getInstance () {
        return Holder.INSTANCE;
    }

    private NumberConverter () {
    }

    @Override
    public Number convert (ParsedValue<String, Number> value, Font __) {
        String val = value.getValue ();
        if (val == null)
            return null;
        if (val.indexOf ('.') != -1) {
            if (val.endsWith ("f"))
                return Float.parseFloat (val);
            else
                return Double.parseDouble (val);
        }
        else if (val.length () > 9)
            return Long.parseLong (val);
        else
            return Integer.parseInt (val);
    }

    @Override
    public String toString () {
        return "NumberConverter";
    }
}
