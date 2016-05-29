package org.chocvanilla.weatherapp.data.observations;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public double getAirTemperature() {
        return airTemperature;
    }

    @Override
    public List<Field> getFields() {
        final String DEG_C = "%.1f °C";
        final String KM_H = "%.1f km/h";
        final String KT = "%.1f kt";
        final String MM = "%.1f mm";
        final SimpleDateFormat DATE = new SimpleDateFormat();

        return Arrays.asList(
                new Field("Time", DATE::format, timestamp, false),
                new Field("Air Temp", DEG_C, airTemperature),
                new Field("Apparent Temp", DEG_C, apparentTemperature),
                new Field("Gust (km/h)", KM_H, gustKm),
                new Field("Gust (kt)", KT, gustKt),
                new Field("Wind Direction", "%s", windDir, false),
                new Field("Wind Speed (km/h)", KM_H, windSpdKmh),
                new Field("Wind Speed (kt)", KT, windSpdKt),
                new Field("Dew Point", DEG_C, dewPt),
                new Field("Rain (mm)", MM, rain)
        );
    }
}

