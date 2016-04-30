package org.chocvanilla.weatherapp.chart;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataHelpers {

    /**
     * Prepare a gson object that can cope with the Bureau of Meteorology data format.
     * @return a configured Gson
     */
    public static Gson observationsGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Float.class, 
                        (JsonDeserializer<Float>) DataHelpers::deserializeFloat)
                .registerTypeAdapter(Date.class,
                        (JsonDeserializer<Date>) DataHelpers::deserializeDate)
                .create();
    }
    
    /**
     * Deserialize a float value. If absent or invalid, 0.0f is returned.
     * Needed to cope with missing Bureau of Meteorology data.
     * See {@link JsonDeserializer} for usage.
     */
    public static Float deserializeFloat(JsonElement json, Type t, JsonDeserializationContext c) {
        try {
            return json.getAsFloat();
        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * Deserialize a Bureau of Meteorology-style timestamp.
     * See {@link JsonDeserializer} for usage.
     */
    public static Date deserializeDate(JsonElement element, Type t, JsonDeserializationContext c) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(element.getAsString());
        } catch (ParseException e) {
            return null;
        }
    }
}
