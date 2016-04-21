package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.WeatherObservation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ObservationDescription {
    private final String label;
    private final String format;
    private final Supplier<?> source;

    public ObservationDescription(String labelText, String format, Supplier<?> source) {
        label = labelText;
        this.source = source;
        this.format = format;
    }

    public static List<ObservationDescription> forObservation(WeatherObservation observation) {
        final String DEG_C = "%.1f °C";
        final String KM_H = "%.1f km/h";
        List<ObservationDescription> list = new ArrayList<>();
        list.add(new ObservationDescription("Air Temp", DEG_C, observation::getAirTemperature));
        list.add(new ObservationDescription("Apparent Temp", DEG_C, observation::getApparentTemperature));
        list.add(new ObservationDescription("Gust", KM_H, observation::getGustKm));
        list.add(new ObservationDescription("Wind Speed", KM_H, observation::getWindSpdKm));
        list.add(new ObservationDescription("Wind Direction", "%s", observation::getWindDir));
        list.add(new ObservationDescription("Rain", "%.1f mm", observation::getRain));
        list.add(new ObservationDescription("Dew point", DEG_C, observation::getDewPt));
        return list;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return String.format(format, source.get());
    }
}
