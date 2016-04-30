package org.chocvanilla.weatherapp.data;

import java.util.concurrent.*;

public class AsyncLoader {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private WeatherStation weatherStation;

    public AsyncLoader(WeatherStation station) {
        weatherStation = station;
    }

    public FutureTask<WeatherObservations> loadAsync() {
        FutureTask<WeatherObservations> task = new FutureTask<>(weatherStation::load);
        executor.execute(task);
        return task;
    }
}
