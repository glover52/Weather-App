package org.chocvanilla.weatherapp.data.observations;

import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WeatherObservationsCache {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final long CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private final WeatherStation station;
    private final ObservationsProvider source;
    private long lastRefreshed;
    private WeatherObservations observations;

    private static final Map<String, WeatherObservationsCache> caches = new HashMap<>();
    
    public static WeatherObservationsCache forStation(WeatherStation station, ObservationsProvider provider) {
        return caches.computeIfAbsent(station.getUniqueID(), 
                id -> new WeatherObservationsCache(station, provider));
    }
    
    public WeatherObservationsCache(WeatherStation station, ObservationsProvider source) {
        log.debug("Created cache for '{}'", station);
        this.station = station;
        this.source = source;
        refresh();
    }
    
    private void refresh() {
        log.debug("Refreshed cache for '{}'", station);
        observations = source.loadObservations(station);
        lastRefreshed = System.currentTimeMillis();
    }
    
    public WeatherObservations get(){
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
