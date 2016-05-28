package org.chocvanilla.weatherapp.io;

import org.chocvanilla.weatherapp.data.forecast.ForecastProvider;
import org.chocvanilla.weatherapp.data.observations.ObservationsProvider;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.assertNotNull;

public class AsyncLoaderTest {

    private AsyncLoader loader;
    private static WeatherStation station;

    @BeforeClass
    public static void setUpClass(){
        // Mock station
        station = new WeatherStation() {
            @Override
            public String getName() {
                return "Station Name";
            }

            @Override
            public String getState() {
                return "STATE";
            }

            @Override
            public boolean isFavourite() {
                return false;
            }

            @Override
            public void setFavourite(boolean favourite) {

            }

            @Override
            public String getUniqueID() {
                return "ID";
            }

            @Override
            public double getLatitude() {
                return 0;
            }

            @Override
            public double getLongitude() {
                return 0;
            }

            @Override
            public int compareTo(WeatherStation o) {
                return 0;
            }
        };

    }

    @Before
    public void setUp(){
        loader = new AsyncLoader(station);
    }

    @Test
    public void loadObservationTest() throws ExecutionException, InterruptedException {

        ObservationsProvider observer = mockStation -> new WeatherObservations();

        FutureTask<WeatherObservations> ft = loader.loadAsync(observer);
        WeatherObservations wo = ft.get();
        assertNotNull(wo);
    }

    @Test
    public void loadForecastTest() throws ExecutionException, InterruptedException {

        ForecastProvider forecast = mockStation -> new WeatherObservations();

        FutureTask<WeatherObservations> ft = loader.loadForecastAsync(forecast);
        WeatherObservations wo = ft.get();
        assertNotNull(wo);
    }
}
