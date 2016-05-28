package org.chocvanilla.weatherapp.data.observations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class WeatherObservationsCache {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final long CACHE_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private final Supplier<WeatherObservations> observationsSource;
    private long lastRefreshed;
    
    private WeatherObservations observations;

    public WeatherObservationsCache(Supplier<WeatherObservations> source) {
        observationsSource = source;
        refresh();
    }
    
    private void refresh() {
        observations = observationsSource.get();
        lastRefreshed = System.currentTimeMillis();
    }
    
    public WeatherObservations get(){
        if (System.currentTimeMillis() - lastRefreshed > CACHE_EXPIRY_MILLIS) {
            refresh();
        }
        return observations;
    }
}
