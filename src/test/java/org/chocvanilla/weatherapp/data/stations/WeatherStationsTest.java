package org.chocvanilla.weatherapp.data.stations;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class WeatherStationsTest {
    private WeatherStations ws;
    private WeatherStationSource source;

    @Before
    public void setUp() {
        source = new WeatherStationsJSONFile(new Gson());
    }

    @Test
    public void loadFromGoodSource() throws IOException {
        ws = WeatherStations.loadFrom(source);
        assertNotNull(ws);
        assertFalse(ws.isEmpty());
    }

    // Testing if IOException correctly propagates through. is this even a test
    @Test(expected = IOException.class)
    public void loadFromBadSource() throws IOException {

        WeatherStationSource badSource = new WeatherStationSource() {
            @Override
            public List<WeatherStation> load() throws IOException {
                throw new IOException();
            }

            @Override
            public void save(WeatherStations stations) throws IOException {
            }
        };

        ws = WeatherStations.loadFrom(badSource);
    }

    @Test
    public void getFavoritesTest() throws IOException {
        ws = WeatherStations.loadFrom(source);

        boolean flag = true;
        for (WeatherStation s : ws) {
            if (flag) s.setFavourite(true);
            flag = !flag;
        }
        ws.getFavourites()
                .filter(WeatherStation::isFavourite)
                .forEach(x -> assertTrue(x.isFavourite()));
    }
}
