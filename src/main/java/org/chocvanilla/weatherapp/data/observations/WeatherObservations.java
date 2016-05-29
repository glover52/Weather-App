package org.chocvanilla.weatherapp.data.observations;

import java.util.*;

public class WeatherObservations extends AbstractCollection<WeatherObservation> {
    private final List<WeatherObservation> observations;

    public WeatherObservations(WeatherObservation... observations) {
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

