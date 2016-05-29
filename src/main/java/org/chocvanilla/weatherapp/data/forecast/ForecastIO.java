package org.chocvanilla.weatherapp.data.forecast;

import com.google.gson.*;
import org.chocvanilla.weatherapp.data.observations.WeatherCache;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.KeyProvider;
import org.chocvanilla.weatherapp.io.MissingAPIKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class ForecastIO implements ForecastProvider {
    public static final String API_CALL_FORMAT = "https://api.forecast.io/forecast/%s/%f,%f" +
            "?units=ca&exclude=currently,minutely,daily,alerts,flags" +
            "&extend=hourly";
    private final Gson gson;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public ForecastIO(Gson gson) {
        this.gson = gson;
    }

    @Override
    public WeatherObservations loadForecast(WeatherStation station) {
        WeatherCache cache = WeatherCache.forecasting(station, this::loadForecastUncached);
        return cache.get();
    }
    
    public WeatherObservations loadForecastUncached(WeatherStation station){
        try {
            return downloadForecastFor(station.getLatitude(), station.getLongitude());
        } catch (IOException e) {
            log.error("Unable to parse forecast for '{}'", station, e);
        } catch (MissingAPIKeyException e) {
            log.error("ForecastIO API key missing,", e);   
        }
        return new WeatherObservations();
    }

    private WeatherObservations downloadForecastFor(double latitude, double longitude) throws IOException {
        String api_key = KeyProvider.getForecastAPIKey();
        String request = 
                String.format(API_CALL_FORMAT, api_key, latitude, longitude);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(request).openStream()))) {
            JsonObject object = (JsonObject) new JsonParser().parse(reader);
            JsonElement data = object.get("hourly").getAsJsonObject().get("data");
            return new WeatherObservations(gson.fromJson(data, ForecastObservation[].class));
        }
    }
}
