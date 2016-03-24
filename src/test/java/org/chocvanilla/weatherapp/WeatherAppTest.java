package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.WeatherStation;
import org.chocvanilla.weatherapp.data.WeatherStations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeatherAppTest {
    WeatherApp underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new WeatherApp();
    }

    @Test
    public void windowIsShown() {
        underTest.run();
        assertTrue(underTest.mainWindow.isVisible());
    }
    
    @Test
    public void weatherStationsAreLoaded() throws IOException {
        WeatherStations db = new WeatherStations();
        List<WeatherStation> stations = db.load();
        assertThat(stations, is(not(empty())));
    }

}
