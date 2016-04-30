package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.*;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import static org.chocvanilla.weatherapp.chart.ChartHelpers.createChart;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

public class WeatherAppTest {
    private WeatherStations db;

    /**
     * Initialisation of some necessary functions to be run before any test. Populates a list of weather stations
     * from the .weather_stations.json file, and loads any favourites stored to file.
     */
    @Before
    public void setUp() throws Exception {
        db = new WeatherStations(new WeatherStationsJSONFile(new Gson()));
    }

    @Test
    public void weatherStationsAreLoaded() throws IOException {
        assertThat(db.getStations(), is(not(empty())));
    }

    @Test
    public void weatherObservationIsLoaded() throws IOException {
        Optional<BomWeatherStation> station = db.firstMatch(x -> hasWmoNumber(x, 94828));
        assertTrue(station.isPresent());
        WeatherObservations observations = station.get().load();
        assertThat(observations, is(not(empty())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadObservationFromNullStation() throws IOException {
        BomWeatherStation station = new BomWeatherStation();
        WeatherObservations observations = station.load();
    }

    @Test
    public void favouritesAreSaved() throws IOException {
        int[] wmoNumbers = {94828, 94302, 95607, 94620, 95625, 94641,};

        for (int wmo : wmoNumbers) {
            db.firstMatch(x -> hasWmoNumber(x, wmo))
                    .ifPresent(x -> x.setFavourite(true));
        }

        db.save(); // fails on exception
    }

    private boolean hasWmoNumber(BomWeatherStation station, int wmo) {
        return station.getWmoNumber() == wmo;
    }

    @Test
    public void favouritesAreLoaded() throws IOException {
        favouritesAreSaved();

        Optional<BomWeatherStation> station = db.firstMatch(x -> x.getWmoNumber() == 94828);
        assertTrue(station.isPresent());
        assertTrue(station.get().isFavourite());
    }

    /**
     * Testing of chart interfaces.
     */
    @Test
    public void canCreateChart() throws IOException {
        assumeFalse(GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance());
        Optional<BomWeatherStation> maybe = db.firstMatch(x -> hasWmoNumber(x, 94828));
        assertTrue(maybe.isPresent());
        BomWeatherStation station = maybe.get();
        WeatherObservations observations = station.load();
        JFreeChart testChart = createChart(station, observations);
        JFrame frame = setUpTestWindow();
        ChartPanel panel = new ChartPanel(testChart);
        frame.add(panel);
        frame.setVisible(true);
        assertTrue(frame.isVisible());
        assertTrue(panel.isVisible());
    }

    private JFrame setUpTestWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("Test Window");
        frame.setSize(800, 600);

        return frame;
    }

}
