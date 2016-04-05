package org.chocvanilla.weatherapp.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class WeatherStations {
    
    private final Path WEATHER_STATIONS_FILE = Paths.get("weather_stations.json");
	
    public List<WeatherStation> load() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(WEATHER_STATIONS_FILE)) {
            Gson gson = new Gson();
            return Arrays.asList(gson.fromJson(reader, WeatherStation[].class));
        }
    }
}

