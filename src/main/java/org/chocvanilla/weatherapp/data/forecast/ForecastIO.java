package org.chocvanilla.weatherapp.data.forecast;

import com.google.gson.*;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.KeyProvider;
import org.chocvanilla.weatherapp.io.MissingAPIKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class ForecastIO implements ForecastProvider {
    private final Gson gson = new Gson();
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public WeatherObservations loadForecast(WeatherStation station) throws MissingAPIKeyException {
        try {
            return downloadForecastFor(station.getLatitude(), station.getLongitude());
        } catch (IOException e) {
            log.error("Unable to parse forecast for '{}'", station, e);
            return new WeatherObservations();
        }
    }

    private WeatherObservations downloadForecastFor(double latitude, double longitude) throws MissingAPIKeyException, IOException {
        String api_key = KeyProvider.getForecastAPIKey();
        String request = String.format("https://api.forecast.io/forecast/%s/%f,%f?units=ca", api_key, latitude, longitude);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(request).openStream()))) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("hourly").getAsJsonObject().get("data");
            return new WeatherObservations(gson.fromJson(data, ForecastObservation[].class));
        }
    }
}
