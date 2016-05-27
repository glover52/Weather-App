package org.chocvanilla.weatherapp.data.forecast;

import com.google.gson.*;
import org.chocvanilla.weatherapp.data.observations.WeatherObservation;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.KeyProvider;

import java.io.*;
import java.net.URL;

public class ForecastIO implements ForecastProvider {

    @Override
    public WeatherObservations getForecast(WeatherStation station) {
        return new WeatherObservations(downloadForecastFor(station.getLatitude(), station.getLongitude()));
    }

    private WeatherObservation[] downloadForecastFor(double latitude, double longitude) {
        String api_key = KeyProvider.getForecastAPIKey();
        String request = String.format("https://api.forecast.io/forecast/%s/%f,%f", api_key, latitude, longitude);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(request).openStream()))) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("hourly").getAsJsonObject().get("data");
            return new Gson().fromJson(data, ForecastObservation[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new WeatherObservation[0];
        }
    }
}
