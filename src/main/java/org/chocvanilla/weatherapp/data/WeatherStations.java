package org.chocvanilla.weatherapp.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class WeatherStations {
    private final Path WEATHER_STATIONS_FILE = Paths.get("weather_stations.json");
    private final List<WeatherStation> stations = new ArrayList<>();

    public List<WeatherStation> getStations() {
        return stations;
    }
    
    public WeatherStation getByWmoNumber(int wmoNumber){
        Optional<WeatherStation> result = stations.stream()
                .filter(station -> station.getWmoNumber() == wmoNumber)
                .findFirst();
        return result.orElseThrow(()-> new RuntimeException("No station with this wmoNumber"));
    }
    
    public void load() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(WEATHER_STATIONS_FILE)) {
            Gson gson = new Gson();
            stations.addAll(Arrays.asList(gson.fromJson(reader, WeatherStation[].class)));
            stations.sort(WeatherStations::compare);
        }
    }
    
    private static int compare(WeatherStation one, WeatherStation two){
        int result = one.getState().compareTo(two.getState());
        if (result != 0){
            return result;
        }
        return one.getName().compareTo(two.getName());
    }
}

