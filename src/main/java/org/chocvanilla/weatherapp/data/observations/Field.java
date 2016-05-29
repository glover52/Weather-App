package org.chocvanilla.weatherapp.data.observations;

import java.util.function.Function;

public class Field {
    private final String label;
    private final String formattedValue;
    private final Object value;

    public Field(String labelText, String format, Object formattedValue) {
        this(labelText, x -> String.format(format, x), formattedValue);
    }
    
    public Field(String labelText, Function<Object, String> formatter, Object obj) {
        label = labelText;
        formattedValue = formatter.apply(obj);
        value = obj;
    }
    
    public Object getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getLabel(), getFormattedValue());
    }
}
