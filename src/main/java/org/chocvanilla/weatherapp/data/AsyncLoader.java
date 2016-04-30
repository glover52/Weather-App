package org.chocvanilla.weatherapp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class AsyncLoader {
    public static final Map<Integer, Long> cache = new HashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    private WeatherStation weatherStation;
    
    public AsyncLoader(WeatherStation station){
        weatherStation = station;
    }

    public FutureTask<WeatherObservations> loadAsync() {
        FutureTask<WeatherObservations> task = new FutureTask<>(weatherStation::load);
        executor.execute(task);
        return task;
    }

    public long msSinceLastRefresh(BomWeatherStation station) {
        Long lastRefresh = cache.getOrDefault(station.getWmoNumber(), null);
        if (lastRefresh == null) {
            return Long.MAX_VALUE;
        }
        long now = System.currentTimeMillis();
        return now - lastRefresh;
    }
}
