package org.chocvanilla.weatherapp.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

import static org.chocvanilla.weatherapp.gui.MessageDialog.messageBox;

public class WeatherStations {
    private static final String WEATHER_STATIONS_FILE = "/.weather_stations.json";
    private final List<BomWeatherStation> stations = new ArrayList<>();

    private WeatherStations(BomWeatherStation... weatherStations) {
        stations.addAll(Arrays.asList(weatherStations));
        stations.sort(WeatherStations::compare);
    }

    public static WeatherStations loadFromFile() {
        try (BufferedReader reader = getStationReader()) {
            Gson gson = new Gson();
            return new WeatherStations(gson.fromJson(reader, BomWeatherStation[].class));
        } catch (IOException error) {
            messageBox("ERROR: Weather Stations file could not be loaded!", "ERROR!");
            return new WeatherStations();
        }
    }

    private static int compare(BomWeatherStation one, BomWeatherStation two) {
        int result = one.getState().compareTo(two.getState());
        if (result != 0) {
            return result;
        }
        return one.getName().compareTo(two.getName());
    }

    public List<BomWeatherStation> getStations() {
        return stations;
    }

    public Optional<BomWeatherStation> firstMatch(Predicate<BomWeatherStation> condition) {
        return stations.stream()
                .filter(condition)
                .findFirst();
    }
    
    private static BufferedReader getStationReader() throws IOException {
        return Files.newBufferedReader(getResource(WeatherStations.class, WEATHER_STATIONS_FILE));
    }
    
    public static Path getResource(Class relativeTo, String name){
        try {
            return Paths.get(relativeTo.getResource(name).toURI());
        } catch (URISyntaxException ignored) {
            throw new Error(ignored);
        }
    }

}

