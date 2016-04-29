package org.chocvanilla.weatherapp.data;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import static org.chocvanilla.weatherapp.gui.MessageDialog.messageBox;

public class ObservationLoader {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final String target = ".observations";
    private static final Map<Integer, Long> cache = new HashMap<>();

    /**
     * Download all available weather observations from the specified station.
     *
     * @param station the source of the weather observations
     * @return a list of observations
     * @throws IOException if an error occurred while attempting the download
     */
    public static List<WeatherObservation> loadObservations(BomWeatherStation station) throws IOException {
        ObservationLoader loader = new ObservationLoader();
        return loader.load(station);
    }

    public static FutureTask<List<WeatherObservation>> loadAsync(BomWeatherStation station) {
        FutureTask<List<WeatherObservation>> task = new FutureTask<>(() -> loadObservations(station));
        executor.execute(task);
        return task;
    }

    public List<WeatherObservation> load(BomWeatherStation station) throws IOException {
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(msSinceLastRefresh(station));
        if (elapsedMinutes > 5) {
            downloadFile(station);
            cache.put(station.getWmoNumber(), System.currentTimeMillis());
        }
        try (BufferedReader reader = Files.newBufferedReader(getPathFor(station))) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("observations").getAsJsonObject().get("data");
            Gson gson = new GsonBuilder().registerTypeAdapter(Float.class,
                    new OptionalDeserializer<>(Float::valueOf, 0.0f)).create();
            return Arrays.asList(gson.fromJson(data, WeatherObservation[].class));
        }
    }

    public static long msSinceLastRefresh(BomWeatherStation station) {
        Long lastRefresh = cache.getOrDefault(station.getWmoNumber(), null);
        if (lastRefresh == null) {
            return Long.MAX_VALUE;
        }
        long now = System.currentTimeMillis();
        return now - lastRefresh;
    }

    private void downloadFile(BomWeatherStation station) {
        try (InputStream in = new URL(station.getUrl()).openStream()) {
            Paths.get(target).toFile().mkdirs();
            Path path = getPathFor(station);
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
            messageBox("ERROR: Unable to establish connection to BOM", "ERROR!");
        }
    }

    private Path getPathFor(BomWeatherStation station) {
        return Paths.get(target, String.valueOf(station.getWmoNumber()) + ".json");
    }

    /**
     * Transforms a list of observations into a multidimensional array of objects
     *
     * @param observations A list of weather observations
     * @return an array of observations
     */
    public static Object[][] observationHistory(List<WeatherObservation> observations) {
        Object[][] data = new Object[observations.size()][10];

        for (int i = 0; i < observations.size(); i++) {
            data[i][0] = observations.get(i).getTimestamp();
            data[i][1] = observations.get(i).getAirTemperature();
            data[i][2] = observations.get(i).getApparentTemperature();
            data[i][3] = observations.get(i).getGustKm();
            data[i][4] = observations.get(i).getGustKt();
            data[i][5] = observations.get(i).getWindDir();
            data[i][6] = observations.get(i).getWindSpdKm();
            data[i][7] = observations.get(i).getWindSpdKt();
            data[i][8] = observations.get(i).getDewPt();
            data[i][9] = observations.get(i).getRain();
        }
        return data;
    }
}


