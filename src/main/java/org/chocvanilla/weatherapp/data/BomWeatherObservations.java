package org.chocvanilla.weatherapp.data;

import org.chocvanilla.weatherapp.data.WeatherObservation;
import org.chocvanilla.weatherapp.data.WeatherObservations;

import java.util.*;

public class BomWeatherObservations extends AbstractCollection<WeatherObservation> implements WeatherObservations {
    private final List<WeatherObservation> observations;

    public BomWeatherObservations(WeatherObservation... observations) {
        this.observations = Arrays.asList(observations);
    }

    @Override
    public Iterator<WeatherObservation> iterator() {
        return observations.iterator();
    }

    @Override
    public int size() {
        return observations.size();
    }
}
