package org.chocvanilla.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class WeatherObservation {
    @SerializedName("local_date_time_full")
    private String timestamp;

    private String name;
    
    @SerializedName("air_temp")
    private double airTemperature;

    public String getTimestamp() {
        return timestamp;
    }

    public double getAirTemperature() {
        return airTemperature;
    }
}

