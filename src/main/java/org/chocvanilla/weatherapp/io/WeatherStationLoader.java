package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.WeatherStation;
import org.chocvanilla.weatherapp.data.WeatherStations;

import java.util.List;

public interface WeatherStationLoader {
    List<WeatherStation> load();

    void save(WeatherStations stations);
}
