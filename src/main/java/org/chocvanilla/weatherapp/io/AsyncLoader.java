package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class AsyncLoader {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final WeatherStation weatherStation;

    public AsyncLoader(WeatherStation station) {
        weatherStation = station;
    }

    public FutureTask<WeatherObservations> loadAsync() {
        log.trace("Loading weather observations for station {} asynchronously", weatherStation);
        FutureTask<WeatherObservations> task = new FutureTask<>(weatherStation::load);
        executor.execute(task);
        return task;
    }
}
