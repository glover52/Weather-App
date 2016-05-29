package org.chocvanilla.weatherapp.data.observations;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.DataHelpers;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;
public class WeatherCacheTest {
    private WeatherCache cache;

    @Before
    public void setUp() throws IOException{
        WeatherStationSource source = new WeatherStationsJSONFile(new Gson());
        WeatherStations ws = WeatherStations.loadFrom(source);
        WeatherStation s = ws.stream()
                .findAny()
                .get();
        BureauOfMeteorology bom = new BureauOfMeteorology(DataHelpers.observationsGson());

        cache = new WeatherCache(s, bom);
    }

    @Test
    public void getCachedObservations() {
        WeatherObservations obs = cache.get();
        assertThat(obs, is(not(empty())));
    }
}
