package org.chocvanilla.weatherapp.data.forecast;

import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;

public interface ForecastProvider {

    WeatherObservations loadForecast(WeatherStation station);
}
