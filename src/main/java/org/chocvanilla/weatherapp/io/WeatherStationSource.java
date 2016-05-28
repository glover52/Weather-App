package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;

import java.io.IOException;
import java.util.List;

public interface WeatherStationSource {
    List<WeatherStation> load() throws IOException;

    void save(WeatherStations stations) throws Exception;
}
