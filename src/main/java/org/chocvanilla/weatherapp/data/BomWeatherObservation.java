package org.chocvanilla.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

// fields are auto-set by Gson
@SuppressWarnings("unused")
public class BomWeatherObservation implements WeatherObservation {
    @SerializedName("local_date_time_full")
    private Date timestamp;

    @SerializedName("name")
    private String name;

    @SerializedName("apparent_t")
    private Float apparentTemperature;

    @SerializedName("gust_kmh")
    private Float gustKm;

    @SerializedName("gust_kt")
    private Float gustKt;

    @SerializedName("air_temp")
    private Float airTemperature;

    @SerializedName("dewpt")
    private Float dewPt;

    @SerializedName("rain_trace")
    private Float rain;

    @SerializedName("wind_dir")
    private String windDir;

    @SerializedName("wind_spd_kmh")
    private Float windSpdKmh;

    @SerializedName("wind_spd_kt")
    private float windSpdKt;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public Float getApparentTemperature() {
        return apparentTemperature;
    }

    public Float getGustKm() {
        return gustKm;
    }

    public Float getGustKt() {
        return gustKt;
    }

    public Float getDewPt() {
        return dewPt;
    }

    public Float getRain() {
        return rain;
    }

    public String getWindDir() {
        return windDir;
    }

    public Float getWindSpdKmh() {
        return windSpdKmh;
    }

    public Float getWindSpdKt() {
        return windSpdKt;
    }

    public Float getAirTemperature() {
        return airTemperature;
    }
}

