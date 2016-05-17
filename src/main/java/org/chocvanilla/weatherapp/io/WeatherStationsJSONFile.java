package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.*;
import org.chocvanilla.weatherapp.gui.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.chocvanilla.weatherapp.io.FileSystemHelpers.getResource;

public class WeatherStationsJSONFile implements WeatherStationSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String STATIONS_PATH = "/.weather_stations.json";
    private static final Path FAVOURITES_PATH = Paths.get(".preferences", "favourites.dat");
    private final Gson gson;

    public WeatherStationsJSONFile(Gson gson) {
        this.gson = gson;
    }

    public List<WeatherStation> load() throws IOException {
        log.debug("Attempting to load favourites");
        Set<String> favourites = new HashSet<>();
        try (BufferedReader favouritesReader = Files.newBufferedReader(FAVOURITES_PATH)) {
            favouritesReader.lines().forEach(favourites::add);
            log.debug("{} favourites loaded", favourites.size());
        } catch  (IOException e) {
            log.error("Unable to favourites from '{}'", FAVOURITES_PATH, e);
        }
        
        log.debug("Attempting to load weather stations");
        try (BufferedReader stationsReader = getResource(getClass(), STATIONS_PATH)) {
            List<WeatherStation> result =
                    Arrays.asList(gson.fromJson(stationsReader, BomWeatherStation[].class));
            
            result.stream()
                    .filter(x -> favourites.contains(x.getUniqueID()))
                    .forEach(station -> station.setFavourite(true));
            log.debug("{} weather stations loaded", result.size());   
            return result;
        }

    }

    public void save(WeatherStations stations) {
        log.debug("Attempting to save favourites");
        try (PrintWriter favouritesWriter = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH))) {
            stations.getFavourites()
                    .map(WeatherStation::getUniqueID)
                    .forEach(favouritesWriter::println);
            log.debug("Favourites saved");
        } catch (IOException e) {
            String message = "Favourites file could not be saved!";
            MessageBox.show(message, "ERROR!");
            log.error(message, e);
        }

    }
}
