package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.WeatherObservations;
import org.chocvanilla.weatherapp.data.WeatherStation;

import java.util.concurrent.*;

public class AsyncLoader {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private final WeatherStation weatherStation;

    public AsyncLoader(WeatherStation station) {
        weatherStation = station;
    }

    public FutureTask<WeatherObservations> loadAsync() {
        FutureTask<WeatherObservations> task = new FutureTask<>(weatherStation::load);
        executor.execute(task);
        return task;
    }
}
