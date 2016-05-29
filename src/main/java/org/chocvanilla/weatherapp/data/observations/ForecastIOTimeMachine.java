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
        try {
            return downloadObservationsFor(station.getLatitude(), station.getLongitude());
        } catch (IOException e) {
            log.error("Unable to parse forecast for '{}'", station, e);
        } catch (MissingAPIKeyException e) {
            log.error("ForecastIO API key missing,", e);
        }
        return new WeatherObservations();
    }

    private WeatherObservations downloadObservationsFor(double latitude, double longitude) throws IOException {
        String api_key = KeyProvider.getForecastAPIKey();
        List<WeatherObservation> obs = new ArrayList<>();
        
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            String request =
                    String.format(API_CALL_FORMAT, api_key, latitude, longitude, 
                            Instant.now().minus(Duration.ofDays(i)).getEpochSecond());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(request).openStream()))) {
                JsonObject object = (JsonObject) new JsonParser().parse(reader);
                JsonElement data = object.get("hourly").getAsJsonObject().get("data");
                obs.addAll(Arrays.asList(gson.fromJson(data, ForecastObservation[].class)));
            }           
        }
        return new WeatherObservations(obs);
    }
}
