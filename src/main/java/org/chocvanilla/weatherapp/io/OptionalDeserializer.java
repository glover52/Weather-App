package org.chocvanilla.weatherapp.io;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Deserializes a JSON field that may contain an invalid value.
 *
 * @param <T> The type of the field to be deserialized.
 */
public class OptionalDeserializer<T> implements JsonDeserializer<T> {
    private final Function<String, T> parser;
    private final T ifUnsuccessful;

    /**
     * Create a new instance which attempts to parse JSON fields.
     *
     * @param parse  the function to convert from a String to the desired type
     * @param orElse the return value if the conversion fails
     */
    public OptionalDeserializer(Function<String, T> parse, T orElse) {
        parser = parse;
        ifUnsuccessful = orElse;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return parser.apply(json.getAsString());
        } catch (Exception e) {
            return ifUnsuccessful;
        }
    }
}
