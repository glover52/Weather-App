package org.chocvanilla.weatherapp;

import org.junit.Before;
import org.junit.Test;

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

}
