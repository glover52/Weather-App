package org.chocvanilla.weatherapp.data.forecast;

import org.junit.Before;

public class ForecastTest {
    private ForecastProvider provider;
    
    @Before
    private void setUp(){
        provider = new ForecastIO();
    }
    
    private void shouldRetrieveForecastForCoordinates(){
        provider.getForecast();
    }
    
}
