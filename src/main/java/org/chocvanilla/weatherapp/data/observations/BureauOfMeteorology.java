package org.chocvanilla.weatherapp.data.observations;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class BureauOfMeteorology implements ObservationsProvider {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static final String URL_FORMAT = "http://www.bom.gov.au/fwo/%s/%s.%s.json";
    public static final String PRODUCT_ID = "ID%c60801";
    public static final String target = ".observations";
    private final FileDownloader downloader = new FileDownloader();
    private final Gson gson;

    public BureauOfMeteorology(Gson gson) {
        this.gson = gson;
    }

    public WeatherObservations loadObservations(WeatherStation station) {
        if (station.getUniqueID() == null) {
            throw new IllegalArgumentException("The supplied WeatherStation does not have a unique id!");
        }
        WeatherCache cache = WeatherCache.observing(station, this::loadObservationsUncached);
        return cache.get();
    }

    /**
     * Download all available weather observations from this station.
     *
     * @return a list of observations
     */
    private WeatherObservations loadObservationsUncached(WeatherStation station) {
        try {
            log.debug("No cached observations. Downloading observation data for {}", station.getName());
            Path path = downloader.downloadFile(buildURL(station), target, station.getUniqueID() + ".json");
            return parseObservations(path);
        } catch (MalformedURLException e) {
            log.error("Malformed URL for station '{}'", station, e);
        } catch (IOException e) {
            log.error("Unable to download latest observations for '{}' ", station, e);
        }
        return new WeatherObservations();
    }

    private WeatherObservations parseObservations(Path path) {
        log.trace("Attempting to parse downloaded JSON from '{}'", path);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("observations").getAsJsonObject().get("data");
            WeatherObservations result = new WeatherObservations(gson.fromJson(data, BomWeatherObservation[].class));
            log.debug("Parsed {} observations from '{}'", result.size(), path);
            return result;
        } catch (IOException e) {
            log.error("Unable to parse observations from '{}'", path, e);
            return new WeatherObservations();
        }
    }

    private String formatProductID(WeatherStation station) {
        return String.format(PRODUCT_ID, station.getState().charAt(0));
    }

    private URL buildURL(WeatherStation station) throws MalformedURLException {
        String productID = formatProductID(station);
        String url = String.format(URL_FORMAT, productID, productID, station.getUniqueID());
        return new URL(url);
    }

}
