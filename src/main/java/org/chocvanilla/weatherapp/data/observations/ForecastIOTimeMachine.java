package org.chocvanilla.weatherapp.data.observations;

import com.google.gson.*;
import org.chocvanilla.weatherapp.data.forecast.ForecastObservation;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.KeyProvider;
import org.chocvanilla.weatherapp.io.MissingAPIKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForecastIOTimeMachine implements ObservationsProvider {
    public static final String API_CALL_FORMAT = "https://api.forecast.io/forecast/%s/%f,%f,%d" +
            "?units=ca" +
            "&exclude=minutely,daily,alerts,flags";
    public static final int NUMBER_OF_DAYS = 14;
    private final Gson gson;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public ForecastIOTimeMachine(Gson gson) {
        this.gson = gson;
    }

    @Override
    public WeatherObservations loadObservations(WeatherStation station) {
        WeatherCache cache = WeatherCache.timeMachining(station, this::loadObservationsUncached);
        return cache.get();
    }

    public WeatherObservations loadObservationsUncached(WeatherStation station) {
        return downloadObservationsFor(station.getLatitude(), station.getLongitude());
    }

    private WeatherObservations downloadObservationsFor(double latitude, double longitude) {
        String apiKey = KeyProvider.getForecastAPIKey();
        final List<WeatherObservation> obs = IntStream.range(0, NUMBER_OF_DAYS)
                .boxed().parallel()
                .map(day -> download(apiKey, day, latitude, longitude))
                .flatMap(Collection::stream)
                .sorted((x,y) -> y.getTimestamp().compareTo(x.getTimestamp()))
                .collect(Collectors.toList());
        return new WeatherObservations(obs);
    }

    private List<WeatherObservation> download(String apiKey, int day, double lat, double lon) {
        try {
            String request = String.format(API_CALL_FORMAT, apiKey, lat, lon,
                    Instant.now().minus(Duration.ofDays(day)).getEpochSecond());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(request).openStream()))) {
                JsonObject object = (JsonObject) new JsonParser().parse(reader);
                JsonElement data = object.get("hourly").getAsJsonObject().get("data");
                return Arrays.asList(gson.fromJson(data, ForecastObservation[].class));
            }
        } catch (IOException e) {
            log.error("Unable to parse ForecastIO observations for '{}', '{}", lat, lon, e);
        } catch (MissingAPIKeyException e) {
            log.error("ForecastIO API key missing,", e);
        }
        return Collections.emptyList();

    }
}
