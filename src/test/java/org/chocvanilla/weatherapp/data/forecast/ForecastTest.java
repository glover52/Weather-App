package org.chocvanilla.weatherapp.data.forecast;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;

import java.io.IOException;

public class ForecastTest {
    private ForecastProvider provider;
    private WeatherStation station;
    
    @Before
    private void setUp() throws IOException {
        provider = new ForecastIO();
        station = new WeatherStationsJSONFile(new Gson()).load().get(0);
    }
    
    private void shouldRetrieveForecastForCoordinates(){
        WeatherObservations forecast = provider.getForecast(station);
    }
    
}
