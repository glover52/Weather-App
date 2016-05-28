package org.chocvanilla.weatherapp.data.observations;

import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class WeatherObservationsCache {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final long CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private final Supplier<WeatherObservations> observationsSource;
    private long lastRefreshed;
    private WeatherObservations observations;

    private static final Map<String, WeatherObservationsCache> caches = new HashMap<>();
    
    public static WeatherObservationsCache forStation(WeatherStation station, Supplier<WeatherObservations> supplier) {
        return caches.computeIfAbsent(station.getUniqueID(), 
                id -> new WeatherObservationsCache(supplier));
    }
    
    public WeatherObservationsCache(Supplier<WeatherObservations> source) {
        observationsSource = source;
        refresh();
    }
    
    private void refresh() {
        observations = observationsSource.get();
        lastRefreshed = System.currentTimeMillis();
    }
    
    public WeatherObservations get(){
        if (msSinceLastRefreshed() > CACHE_EXPIRY_MILLIS) {
            refresh();
        }
        return observations;
    }
    
    public long msSinceLastRefreshed() {
        return System.currentTimeMillis() - lastRefreshed;
    }
    

}
