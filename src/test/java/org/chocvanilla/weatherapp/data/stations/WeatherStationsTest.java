package org.chocvanilla.weatherapp.data.stations;

import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class WeatherStationsTest {
    private WeatherStations ws;
    private WeatherStationSource source;


    @Before
    public void setUp() {

        source = new WeatherStationsJSONFile(new Gson());

    }

    @Test
    public void loadFromSource() throws IOException {
        ws = WeatherStations.loadFrom(source);
        assertNotNull(ws);
        assertFalse(ws.isEmpty());
    }

    @Test
    public void getFavoritesTest() throws IOException {
        ws = WeatherStations.loadFrom(source);

        boolean flag = true;
        for ( WeatherStation s : ws) {
            if (flag) s.setFavourite(true); flag = !flag;
        }
        ws.getFavourites()
                .filter(WeatherStation::isFavourite)
                .forEach( x-> {
                    assertTrue(x.isFavourite());
                });
    }
}
