package org.chocvanilla.weatherapp.data.observations;

import org.chocvanilla.weatherapp.data.forecast.ForecastProvider;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WeatherCache {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final long CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private final WeatherStation station;
    private final ObservationsProvider source;
    private long lastRefreshed;
    private WeatherObservations observations;

    private static final Map<String, WeatherCache> observationCaches = new HashMap<>();
    private static final Map<String, WeatherCache> forecastCaches = new HashMap<>();
    private static final Map<String, WeatherCache> timeMachineCaches = new HashMap<>();

    public static WeatherCache observing(WeatherStation station, ObservationsProvider provider) {
        return observationCaches.computeIfAbsent(station.getUniqueID(),
                id -> new WeatherCache(station, provider));
    }

    public static WeatherCache forecasting(WeatherStation station, ForecastProvider provider) {
        return forecastCaches.computeIfAbsent(station.getUniqueID(),
                id -> new WeatherCache(station, provider::loadForecast));
    }

    public static WeatherCache timeMachining(WeatherStation station, ObservationsProvider provider) {
        return timeMachineCaches.computeIfAbsent(station.getUniqueID(),
                id -> new WeatherCache(station, provider));
    }

    public WeatherCache(WeatherStation station, ObservationsProvider source) {
        log.debug("Created cache for '{}'", station);
        this.station = station;
        this.source = source;
    }

    private void refresh() {
        log.debug("Refreshed cache for '{}'", station);
        observations = source.loadObservations(station);
        lastRefreshed = System.currentTimeMillis();
    }

    public WeatherObservations get() {
        if (msSinceLastRefreshed() > CACHE_EXPIRY_MILLIS) {
            refresh();
        } else {
            log.debug("Cached observations retrieved for '{}'", station);
        }
        return observations;
    }

    public long msSinceLastRefreshed() {
        return System.currentTimeMillis() - lastRefreshed;
    }


}
