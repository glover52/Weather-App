package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.BomWeatherStation;
import org.chocvanilla.weatherapp.data.WeatherStations;

import java.util.List;

public interface WeatherStationLoader {
    List<BomWeatherStation> load();

    void save(WeatherStations stations);
}
