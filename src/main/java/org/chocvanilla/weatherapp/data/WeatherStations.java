package org.chocvanilla.weatherapp.data;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WeatherStations {
    private final List<BomWeatherStation> stations = new ArrayList<>();
    private final WeatherStationLoader stationLoader;
    
    public WeatherStations(WeatherStationLoader loader){
        stationLoader = loader;
        stations.addAll(loader.load());
        stations.sort(WeatherStations::compare);
    }
    
    public void save() {
        stationLoader.save(this);
    }

    public List<BomWeatherStation> getStations() {
        return stations;
    }

    public Optional<BomWeatherStation> firstMatch(Predicate<BomWeatherStation> condition) {
        return stations.stream()
                .filter(condition)
                .findFirst();
    }

    public Stream<BomWeatherStation> getFavourites() {
        return stations.stream().filter(BomWeatherStation::isFavourite);
    }

    private static int compare(BomWeatherStation one, BomWeatherStation two) {
        int result = one.getState().compareTo(two.getState());
        if (result != 0) {
            return result;
        }
        return one.getName().compareTo(two.getName());
    }
}

