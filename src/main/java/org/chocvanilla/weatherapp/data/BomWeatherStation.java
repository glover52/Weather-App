package org.chocvanilla.weatherapp.data;


import com.google.gson.*;
import org.chocvanilla.weatherapp.io.AsyncLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

// fields are auto-set by Gson
@SuppressWarnings("unused") 
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

    @Override
    public int compareTo(WeatherStation two) {
        int result = getState().compareTo(two.getState());
        if (result != 0) {
            return result;
        }
        return getName().compareTo(two.getName());
    }


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

    @Override
    public String getUniqueID() {
        return String.valueOf(wmoNumber);
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
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Float.class,
                    (JsonDeserializer<Float>) BomWeatherStation::deserializeFloat)
                    .registerTypeAdapter(Date.class,
                    (JsonDeserializer<Date>) BomWeatherStation::deserializeDate)
                    .create();
            return new BomWeatherObservations(gson.fromJson(data, BomWeatherObservation[].class));
        }
    }
    
    public FutureTask<WeatherObservations> loadAsync() {
        return loader.loadAsync();
    }

    private void downloadFile() throws IOException {
        try (InputStream in = new URL(getUrl()).openStream()) {
            Paths.get(target).toFile().mkdirs();
            Path path = getPath();
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Path getPath() {
        return Paths.get(target, String.valueOf(wmoNumber) + ".json");
    }

    /**
     * Deserialize a float value. If absent or invalid, 0.0f is returned.
     * Needed to cope with missing Bureau of Meteorology data.
     * See {@link JsonDeserializer} for usage.
     */
    private static Float deserializeFloat(JsonElement json, Type t, JsonDeserializationContext c) {
        try {
            return json.getAsFloat();
        } catch (Exception e) {
            return 0.0f;
        }
    }

    private static Date deserializeDate(JsonElement element, Type t, JsonDeserializationContext c) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(element.getAsString());
        } catch (ParseException e) {
            return null;
        }
    }

}
