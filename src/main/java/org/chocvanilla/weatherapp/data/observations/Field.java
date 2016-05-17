package org.chocvanilla.weatherapp.data.observations;

import java.util.function.Function;

public class Field {
    private final String label;
    private final String value;

    public Field(String labelText, String format, Object value) {
        this(labelText, x -> String.format(format, x), value);
    }
    
    public Field(String labelText, Function<Object, String> formatter, Object obj) {
        label = labelText;
        value = formatter.apply(obj);
    }

    public String getLabel() {
        return label;
    }

    public String getFormattedValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getLabel(), getFormattedValue());
    }
}
