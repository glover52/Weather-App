package org.chocvanilla.weatherapp.data;


import com.google.gson.*;
import org.chocvanilla.weatherapp.io.OptionalDeserializer;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.chocvanilla.weatherapp.gui.MessageDialog.messageBox;

public class BomWeatherStation implements WeatherStation {
    public static final String target = ".observations";
    private static final String URL_FORMAT = "http://www.bom.gov.au/fwo/%s/%s.%d.json";
    private static final String PRODUCT_ID = "ID%c60801";
    
    private final AsyncLoader loader = new AsyncLoader(this);

    private int stationID;
    private int wmoNumber;
    private String name;
    private String state;
    private char code;
    
    private boolean favourite;

    /**
     * Last refresh timestamp, not loaded from JSON
     */
    private transient long lastRefreshed;


    public int getStationID() {
        return stationID;
    }

    public int getWmoNumber() {
        return wmoNumber;
    }


    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public char getCode() {
        return code;
    }

    private String formatProductID() {
        return String.format(PRODUCT_ID, getCode());
    }

    public String getUrl() {
        String productID = formatProductID();
        return String.format(URL_FORMAT, productID, productID, wmoNumber);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, state);
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public long msSinceLastRefresh() {
        return System.currentTimeMillis() - lastRefreshed;
    }


    /**
     * Download all available weather observations from this station.
     *
     * @return a list of observations
     * @throws IOException if an error occurred while attempting the download
     */
    public WeatherObservations load() throws IOException {
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(msSinceLastRefresh());
        if (elapsedMinutes > 5) {
            downloadFile();
            lastRefreshed = System.currentTimeMillis();
        }
        try (BufferedReader reader = Files.newBufferedReader(getPath())) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("observations").getAsJsonObject().get("data");
            Gson gson = new GsonBuilder().registerTypeAdapter(Float.class,
                    new OptionalDeserializer<>(Float::valueOf, 0.0f)).create();
            return new BomWeatherObservations(gson.fromJson(data, BomWeatherObservation[].class));
        }
    }
    
    public FutureTask<WeatherObservations> loadAsync(){
        return loader.loadAsync();
    }

    private void downloadFile() {
        try (InputStream in = new URL(getUrl()).openStream()) {
            Paths.get(target).toFile().mkdirs();
            Path path = getPath();
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
            messageBox("ERROR: Unable to establish connection to BOM", "ERROR!");
        }
    }

    private Path getPath() {
        return Paths.get(target, String.valueOf(wmoNumber) + ".json");
    }
}
