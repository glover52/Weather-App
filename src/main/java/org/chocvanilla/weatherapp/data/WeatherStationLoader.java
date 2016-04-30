package org.chocvanilla.weatherapp.data;

import java.util.List;

public interface WeatherStationLoader {
    List<BomWeatherStation> load();

    void save(WeatherStations stations);
}
