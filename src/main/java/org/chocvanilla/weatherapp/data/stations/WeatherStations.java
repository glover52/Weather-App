package org.chocvanilla.weatherapp.data.stations;

import org.chocvanilla.weatherapp.io.WeatherStationSource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class WeatherStations extends AbstractCollection<WeatherStation> {
    private final List<WeatherStation> stations = new ArrayList<>();
    private final WeatherStationSource dataSource;

    private WeatherStations(WeatherStationSource source) {
        dataSource = source;
    }

    public static WeatherStations loadFrom(WeatherStationSource source) throws IOException {
        WeatherStations result = new WeatherStations(source);
        result.stations.addAll(source.load());
        result.stations.sort(null);
        return result;
    }

    public void save() {
        dataSource.save(this);
    }

    public Stream<WeatherStation> getFavourites() {
        return stations.stream().filter(WeatherStation::isFavourite);
    }

    @Override
    public Iterator<WeatherStation> iterator() {
        return stations.iterator();
    }

    @Override
    public int size() {
        return stations.size();
    }
}

