package org.chocvanilla.weatherapp.data.observations;

import org.chocvanilla.weatherapp.data.stations.WeatherStation;

public interface ObservationsProvider {
    WeatherObservations loadObservations(WeatherStation station);
}
