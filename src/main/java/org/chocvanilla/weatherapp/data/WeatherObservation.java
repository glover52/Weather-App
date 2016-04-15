package org.chocvanilla.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherObservation {
    @SerializedName("local_date_time_full")
    private String timestamp;

    @SerializedName("name")
    private String name;

    @SerializedName("apparent_t")
    private float apparentTemprature;

    @SerializedName("gust_kmh")
    private float gustKm;

    @SerializedName("gust_kt")
    private float gustKt;

    @SerializedName("air_temp")
    private float airTemperature;

    @SerializedName("dewpt")
    private float dewPt;

    @SerializedName("rain_trace")
    private float rain;

    @SerializedName("wind_dir")
    private String windDir;

    @SerializedName("wind_spd_kmh")
    private float windSpdKm;

    @SerializedName("wind_spd_kt")
    private float windSpdKt;

    public Date getTimestamp() {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getName(){
        return name;
    }

    public float getApparentTemprature() {
        return apparentTemprature;
    }

    public float getGustKm() {
        return gustKm;
    }

    public float getGustKt() {
        return gustKt;
    }

    public float getDewPt() {
        return dewPt;
    }

    public float getRain() {
        return rain;
    }

    public String getWindDir() {
        return windDir;
    }

    public float getWindSpdKm() {
        return windSpdKm;
    }

    public float getWindSpdKt() {
        return windSpdKt;
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

