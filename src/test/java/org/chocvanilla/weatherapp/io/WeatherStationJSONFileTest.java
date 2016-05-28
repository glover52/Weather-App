package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class WeatherStationJSONFileTest {
    private final Gson gson = new Gson();
    private WeatherStationsJSONFile source;

    @Before
    public void setUp() {
        source = new WeatherStationsJSONFile(gson);
    }

    @Test
    public void testLoad() throws IOException{
        List<WeatherStation> ws = source.load();
        assertNotNull(ws);
    }


    @Test
    public void weatherStationsJSONFileIsPresent() throws IOException {
        WeatherStations stations = WeatherStations.loadFrom(source);
        assertThat(stations, is(not(empty())));
    }
}
