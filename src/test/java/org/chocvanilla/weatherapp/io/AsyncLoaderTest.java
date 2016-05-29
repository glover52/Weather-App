package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.forecast.ForecastProvider;
import org.chocvanilla.weatherapp.data.observations.ObservationsProvider;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.assertNotNull;

public class AsyncLoaderTest {

    private AsyncLoader loader;
    private static WeatherStation station;
    private static Gson gson = new Gson();
    private static WeatherStationsJSONFile source = new WeatherStationsJSONFile(gson);

    @BeforeClass
    public static void setUpClass() throws IOException {
        station = WeatherStations.loadFrom(source).stream()
                .findAny()
                .get();
    }

    @Before
    public void setUp() {
        loader = new AsyncLoader(station);
    }

    @Test
    public void loadObservationTest() throws ExecutionException, InterruptedException {
        ObservationsProvider observer = mockStation -> new WeatherObservations();
        FutureTask<WeatherObservations> ft = loader.loadAsync(observer::loadObservations);
        WeatherObservations wo = ft.get();
        assertNotNull(wo);
    }

    @Test
    public void loadForecastTest() throws ExecutionException, InterruptedException {
        ForecastProvider forecast = mockStation -> new WeatherObservations();
        FutureTask<WeatherObservations> ft = loader.loadAsync(forecast::loadForecast);
        WeatherObservations wo = ft.get();
        assertNotNull(wo);
    }
}
