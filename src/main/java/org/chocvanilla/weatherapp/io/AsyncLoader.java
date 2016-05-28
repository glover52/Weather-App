package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.observations.BureauOfMeteorology;
import org.chocvanilla.weatherapp.data.forecast.ForecastProvider;
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
        FutureTask<WeatherObservations> task = new FutureTask<>(() -> new BureauOfMeteorology().loadObservations(weatherStation));
        executor.execute(task);
        return task;
    }
    
    public FutureTask<WeatherObservations> loadForecastAsync(ForecastProvider provider) {
        log.trace("Loading weather forecast for station {} asynchronously", weatherStation);
        FutureTask<WeatherObservations> task = new FutureTask<>(() -> provider.loadForecast(weatherStation));
        executor.execute(task);
        return task;
    }
}
