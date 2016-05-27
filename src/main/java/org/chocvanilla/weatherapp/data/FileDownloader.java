package org.chocvanilla.weatherapp.data;

import com.google.gson.*;
import org.chocvanilla.weatherapp.data.observations.BomWeatherObservation;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class FileDownloader {
    public static final String URL_FORMAT = "http://www.bom.gov.au/fwo/%s/%s.%s.json";
    public static final String PRODUCT_ID = "ID%c60801";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static final String target = ".observations";
    private static final long CACHE_EXPIRY_MILLIS = TimeUnit.SECONDS.toMillis(5);
    private final WeatherStation station;
    /**
     * Last refresh timestamp, not loaded from JSON
     */
    private long lastRefreshed;
    
    public FileDownloader(WeatherStation station){
        this.station = station;
    }

    private Path getPath() {
        return Paths.get(target, String.valueOf(station.getUniqueID()) + ".json");
    }

    /**
     * Download all available weather observations from this station.
     *
     * @return a list of observations
     */
    public WeatherObservations load()  {
        if (System.currentTimeMillis() - lastRefreshed > CACHE_EXPIRY_MILLIS) {
            log.trace("Attempting to download weather observations for '{}'", station);
            downloadFile();
            lastRefreshed = System.currentTimeMillis();
        }
        log.trace("Attempting to parse downloaded JSON for '{}'", station);
        try (BufferedReader reader = Files.newBufferedReader(getPath())) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("observations").getAsJsonObject().get("data");
            Gson gson = DataHelpers.observationsGson();
            WeatherObservations result = new WeatherObservations(gson.fromJson(data, BomWeatherObservation[].class));
            log.debug("Parsed {} observations from '{}'", result.size(), station);
            return result;
        } catch (IOException e) {
            log.error("Unable to parse observations for '{}'", station, e);
            return new WeatherObservations();
        }
    }

    private void downloadFile() {
        try (InputStream in = new URL(getUrl()).openStream()) {
            Paths.get(target).toFile().mkdirs();
            Path path = getPath();
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Unable to download JSON from '{}' to '{}'", getUrl(), getPath());
        }
    }

    private String formatProductID() {
        return String.format(FileDownloader.PRODUCT_ID, station.getState().charAt(0));
    }

    private String getUrl() {
        String productID = formatProductID();
        return String.format(FileDownloader.URL_FORMAT, productID, productID, station.getUniqueID());
    }
}
