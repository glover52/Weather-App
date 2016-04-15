package org.chocvanilla.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherObservation {
    @SerializedName("local_date_time_full")
    private String timestamp;

    private String name;

    @SerializedName("air_temp")
    private float airTemperature;

    public Date getTimestamp() {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

    public float getAirTemperature() {
        return airTemperature;
    }

    @Override
    public String toString() {
        return String.format("WeatherObservation{timestamp='%s', name='%s', airTemperature=%s}", 
                timestamp, name, airTemperature);
    }
}

