package org.chocvanilla.weatherapp.data;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class ObservationLoader {
    private static final String target = "observations";

    public List<WeatherObservation> load(WeatherStation station) throws IOException {
        downloadFile(station);
        try (BufferedReader reader = Files.newBufferedReader(getPathFor(station))) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("observations").getAsJsonObject().get("data");
            Gson gson = new Gson();
            return Arrays.asList(gson.fromJson(data, WeatherObservation[].class));
        }
    }

    private void downloadFile(WeatherStation station) {
        try (InputStream in = new URL(station.getUrl()).openStream()) {
            Paths.get(target).toFile().mkdirs();
            Path path = getPathFor(station);
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
        }
    }

    private Path getPathFor(WeatherStation station) {
        return Paths.get(target, String.valueOf(station.getWmoNumber()) + ".json");
    }
}


