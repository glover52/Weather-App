package org.chocvanilla.weatherapp.data.forecast;

import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;

public class OpenWeatherMap implements ForecastProvider {

    @Override
    public WeatherObservations getForecast(WeatherStation station) {
        return null;
    }
}
