package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.WeatherStation;
import org.chocvanilla.weatherapp.data.WeatherStations;

import java.io.IOException;
import java.util.List;

public interface WeatherStationSource {
    List<WeatherStation> load() throws IOException;

    void save(WeatherStations stations);
}
