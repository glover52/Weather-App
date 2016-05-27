package org.chocvanilla.weatherapp.data.forecast;

import org.chocvanilla.weatherapp.data.observations.Field;
import org.chocvanilla.weatherapp.data.observations.WeatherObservation;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@SuppressWarnings("unused")
public class ForecastObservation implements WeatherObservation {
    private long time;
    private String summary;
    private String icon;
    private double precipIntensity;
    private double precipProbability;
    private double temperature;
    private double apparentTemperature;
    private double dewPoint;
    private double humidity;
    private double windSpeed;
    private int windBearing;
    private double cloudCover;
    private double pressure;

    @Override
    public Date getTimestamp() {
        return Date.from(Instant.ofEpochSecond(time));
    }

    @Override
    public double getAirTemperature() {
        return temperature;
    }

    @Override
    public List<Field> getFields() {
        final String DEG_C = "%.1f °C";
        final String KM_H = "%.1f km/h";
        final String KT = "%.1f kt";
        final String MM = "%.1f mm";
        final String PERCENT = "%.0f%%";
        
        final SimpleDateFormat DATE = new SimpleDateFormat();

        return Arrays.asList(
                new Field("Time", DATE::format, getTimestamp()),
                new Field("Air Temp", DEG_C, temperature),
                new Field("Apparent Temp", DEG_C, apparentTemperature),
                new Field("Wind Bearing", "%d°", windBearing),
                new Field("Wind Speed (km/h)", KM_H, windSpeed),
                new Field("Dew Point", DEG_C, dewPoint),
                new Field("Rain (mm)", MM, precipIntensity),
                new Field("Chance of rain (%)", PERCENT, precipProbability * 100),
                new Field("Humidity (%)", PERCENT, humidity * 100),
                new Field("Cloud Cover", PERCENT, cloudCover * 100),
                new Field("Pressure (millibars)", "%.0f mbar", pressure),
                new Field("Summary", "%s", summary),
                new Field("Icon", "%s", icon)
        );
    }
}
