package org.chocvanilla.weatherapp.data.observations;

import java.util.function.Function;

public class Field {
    private final String label;
    private final String formattedValue;
    private final Object value;
    private final boolean graphable;

    public Field(String labelText, String format, Object formattedValue) {
        this(labelText, format, formattedValue, true);
    }

    public Field(String labelText, Function<Object, String> formatter, Object obj, boolean isGraphable) {
        label = labelText;
        if (obj == null){
            formattedValue = "N/A";
            value = 0;
            graphable = false;
        } else {
            formattedValue = formatter.apply(obj);
            graphable = isGraphable;
            value = obj;
        }
    }

    public Field(String labelText, String format, Object value, boolean isGraphable) {
        this(labelText, x -> String.format(format, x), value, isGraphable);
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

    public boolean isGraphable() {
        return graphable;
    }
}
