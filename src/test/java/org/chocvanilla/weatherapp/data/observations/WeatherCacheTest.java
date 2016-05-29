package org.chocvanilla.weatherapp.data.observations;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.DataHelpers;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;

public class WeatherCacheTest {
    private WeatherCache cache;
    private static BureauOfMeteorology bom;
    private static WeatherStationSource source;
    private static WeatherStations ws;
    private static WeatherStation s;


    @BeforeClass
    public static void setUpClass() throws IOException {
        bom = new BureauOfMeteorology(DataHelpers.observationsGson());
        source = new WeatherStationsJSONFile(new Gson());
        ws = WeatherStations.loadFrom(source);
        s = ws.stream()
                .findAny()
                .get();

    }

    @Before
    public void setUp() throws IOException {
        cache = new WeatherCache(s, bom);
    }

    @Test
    public void getCachedObservations() {
        WeatherObservations obs = cache.get();
        assertThat(obs, is(not(empty())));
    }

    @Test
    public void cacheExpiredTest () {

        cache = new WeatherCache(s, bom) {
            @Override
            public long msSinceLastRefreshed() {
                return TimeUnit.MINUTES.toMillis(5) + 1;
            }
        };

        WeatherObservations obs = cache.get();
        assertThat(obs, is(not(empty())));
    }
}
