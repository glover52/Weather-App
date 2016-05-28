package org.chocvanilla.weatherapp.data.forecast.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

import java.io.IOException;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;


public class WeatherStationJSONFileTeset {
    private final Gson gson = new Gson();


    @Test
    public void loadFromJSON() {

    }

    @Test
    public void weatherStationsJSONFileIsPresent() throws IOException {
        WeatherStationSource source = new WeatherStationsJSONFile(gson);
        WeatherStations stations = WeatherStations.loadFrom(source);
        assertThat(stations, is(not(empty())));
    }
}
