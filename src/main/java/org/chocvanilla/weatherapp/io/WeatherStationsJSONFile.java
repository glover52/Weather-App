package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.*;
import org.chocvanilla.weatherapp.gui.MessageBox;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.chocvanilla.weatherapp.io.FileSystemHelpers.getResource;

public class WeatherStationsJSONFile implements WeatherStationSource {
    private static final String STATIONS_PATH = "/.weather_stations.json";
    private static final Path FAVOURITES_PATH = Paths.get(".preferences", "favourites.dat");
    private final Gson gson;

    public WeatherStationsJSONFile(Gson gson) {
        this.gson = gson;
    }

    public List<WeatherStation> load() throws IOException {
        try (BufferedReader favouritesReader = Files.newBufferedReader(FAVOURITES_PATH);
             BufferedReader stationsReader = getResource(getClass(), STATIONS_PATH)) {
            
            Set<String> favourites = favouritesReader.lines()
                    .collect(Collectors.toCollection(HashSet::new));
            
            List<WeatherStation> result =
                    Arrays.asList(gson.fromJson(stationsReader, BomWeatherStation[].class));
            
            result.stream()
                    .filter(x -> favourites.contains(x.getUniqueID()))
                    .forEach(station -> station.setFavourite(true));
            
            return result;
        }
    }

    public void save(WeatherStations stations) {
        try (PrintWriter favouritesWriter = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH))) {
            stations.getFavourites()
                    .map(WeatherStation::getUniqueID)
                    .forEach(favouritesWriter::println);
        } catch (IOException error) {
            MessageBox.show("ERROR: Weather stations file could not be saved!", "ERROR!");
        }
    }
}
