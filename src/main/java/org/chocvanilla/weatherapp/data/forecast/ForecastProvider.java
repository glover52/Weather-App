package org.chocvanilla.weatherapp.data.forecast;

import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.MissingAPIKeyException;

public interface ForecastProvider {

    WeatherObservations loadForecast(WeatherStation station) throws MissingAPIKeyException;
}

