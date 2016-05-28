package org.chocvanilla.weatherapp.data.forecast;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.MissingAPIKeyException;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsNot.not;

public class ForecastTest {
    private ForecastProvider provider;
    private WeatherStation station;
    
    @Before
    public void setUp() throws IOException {
        Gson gson = new Gson();
        provider = new ForecastIO(gson);
        station = new WeatherStationsJSONFile(gson).load().get(0);
    }
    
    @Test
    public void shouldRetrieveForecastForCoordinates() throws MissingAPIKeyException {
        WeatherObservations forecast = provider.loadForecast(station);
        assertThat(forecast, is(not(empty())));
    }
    
}
