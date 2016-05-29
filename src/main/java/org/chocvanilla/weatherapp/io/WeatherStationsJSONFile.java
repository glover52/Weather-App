package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.BomWeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.chocvanilla.weatherapp.io.FileSystemHelpers.getResource;

public class WeatherStationsJSONFile implements WeatherStationSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String STATIONS_PATH = "/.weather_stations.json";
    private static final String TARGET = ".preferences";
    private static final Path FAVOURITES_PATH = Paths.get(TARGET, "favourites.dat");
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
        } catch (IOException e) {
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

    public void save(WeatherStations stations) throws IOException {
        Paths.get(TARGET).toFile().mkdirs();
        log.debug("Attempting to save favourites");
        PrintWriter favouritesWriter = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH));
        stations.getFavourites()
                .map(WeatherStation::getUniqueID)
                .forEach(favouritesWriter::println);
        favouritesWriter.flush();
        log.debug("Favourites saved");

    }
}
