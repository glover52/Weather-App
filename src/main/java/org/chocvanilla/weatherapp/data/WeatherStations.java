package org.chocvanilla.weatherapp.data;

import org.chocvanilla.weatherapp.io.WeatherStationSource;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WeatherStations {
    private final List<WeatherStation> stations = new ArrayList<>();
    private final WeatherStationSource stationLoader;

    public WeatherStations(WeatherStationSource loader) {
        stationLoader = loader;
        stations.addAll(loader.load());
        stations.sort(null);
    }

    public void save() {
        stationLoader.save(this);
    }

    public List<WeatherStation> getStations() {
        return stations;
    }

    public Optional<WeatherStation> firstMatch(Predicate<WeatherStation> condition) {
        return stations.stream()
                .filter(condition)
                .findFirst();
    }

    public Stream<WeatherStation> getFavourites() {
        return stations.stream().filter(WeatherStation::isFavourite);
    }
}

