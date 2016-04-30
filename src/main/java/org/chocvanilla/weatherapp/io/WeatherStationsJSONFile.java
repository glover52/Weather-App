package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.chocvanilla.weatherapp.gui.MessageDialog.messageBox;
import static org.chocvanilla.weatherapp.io.FileSystemHelpers.getResource;

public class WeatherStationsJSONFile implements WeatherStationLoader {
    private static final String WEATHER_STATIONS_FILE = "/.weather_stations.json";
    private static final Path FAVOURITES_FILE = Paths.get(".preferences", "favourites.dat");
    private final Gson gson;

    public WeatherStationsJSONFile(Gson gson) {
        this.gson = gson;
    }

    public List<BomWeatherStation> load() {
        try (BufferedReader stationsReader = getResource(getClass(), WEATHER_STATIONS_FILE);
             BufferedReader favouritesReader = Files.newBufferedReader(FAVOURITES_FILE)) {
            Set<Integer> favourites = favouritesReader.lines()
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(HashSet::new));
            List<BomWeatherStation> result =
                    Arrays.asList(gson.fromJson(stationsReader, BomWeatherStation[].class));
            result.stream()
                    .filter(x -> favourites.contains(x.getWmoNumber()))
                    .forEach(station -> station.setFavourite(true));
            return result;
        } catch (IOException error) {
            messageBox("ERROR: Weather stations file could not be loaded!", "ERROR!");
            return Collections.emptyList();
        }
    }

    public void save(WeatherStations stations) {
        try (PrintWriter favouritesWriter = new PrintWriter(Files.newBufferedWriter(FAVOURITES_FILE))) {
            stations.getFavourites()
                    .map(BomWeatherStation::getWmoNumber)
                    .forEach(favouritesWriter::println);
        } catch (IOException error) {
            messageBox("ERROR: Weather stations file could not be saved!", "ERROR!");
        }
    }
}
