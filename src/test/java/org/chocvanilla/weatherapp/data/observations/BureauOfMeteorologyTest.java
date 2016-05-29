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

import static org.junit.Assert.assertNotNull;

public class BureauOfMeteorologyTest {
    private BureauOfMeteorology bom;

    @Before
    public void setUp(){
        bom = new BureauOfMeteorology(DataHelpers.observationsGson());
    }

    @Test
    public void loadObservationsTest() throws IOException {
        WeatherStationSource source = new WeatherStationsJSONFile(new Gson());
        WeatherStations ws = WeatherStations.loadFrom(source);
        WeatherStation s = ws.stream()
                .findAny()
                .get();
        WeatherObservations ob =  bom.loadObservations(s);

        assertNotNull(ob);
    }
}
