package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.observations.*;
import org.chocvanilla.weatherapp.data.stations.*;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

import static org.chocvanilla.weatherapp.chart.ChartHelpers.createChart;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

public class WeatherAppTest {
    private WeatherStations db;
    private ObservationsProvider bom;

    /**
     * Initialisation of some necessary functions to be show before any test. Populates a list of weather stations
     * from the .weather_stations.json file, and loads any favourites stored to file.
     */
    @Before
    public void setUp() throws Exception {
        db = WeatherStations.loadFrom(new WeatherStationsJSONFile(new Gson()));
        bom = new BureauOfMeteorology();
    }

    @Test
    public void weatherStationsAreLoaded() throws IOException {
        assertThat(db, is(not(empty())));
    }
    
    private Optional<WeatherStation> firstMatch(Predicate<WeatherStation> condition) {
        return db.stream().filter(condition).findFirst();
    }

    @Test
    public void weatherObservationIsLoaded() throws IOException {
        Optional<WeatherStation> station = firstMatch(x -> hasWmoNumber(x, 94828));
        assertTrue(station.isPresent());
        WeatherObservations observations = bom.loadObservations(station.get());
        assertThat(observations, is(not(empty())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadObservationFromNullStation() throws IOException {
        WeatherStation station = new BomWeatherStation();
        WeatherObservations observations = bom.loadObservations(station);
    }

    @Test
    public void favouritesAreSaved() throws IOException {
        int[] wmoNumbers = {94828, 94302, 95607, 94620, 95625, 94641,};

        for (int wmo : wmoNumbers) {
            firstMatch(x -> hasWmoNumber(x, wmo)).ifPresent(x -> x.setFavourite(true));
        }

        db.save(); // fails on exception
    }

    private boolean hasWmoNumber(WeatherStation station, int wmo) {
        return station.getUniqueID().equals(String.valueOf(wmo));
    }

    @Test
    public void favouritesAreLoaded() throws IOException {
        favouritesAreSaved();

        Optional<WeatherStation> station = firstMatch(x -> hasWmoNumber(x, 94828));
        assertTrue(station.isPresent());
        assertTrue(station.get().isFavourite());
    }

    /**
     * Testing of chart interfaces.
     */
    @Test
    public void canCreateChart() throws IOException {
        assumeFalse(GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance());
        Optional<WeatherStation> maybe = firstMatch(x -> hasWmoNumber(x, 94828));
        assertTrue(maybe.isPresent());
        WeatherStation station = maybe.get();
        WeatherObservations observations = bom.loadObservations(station);
        JFreeChart testChart = createChart(station, observations, new ArrayList<>());
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
    
    @Test
    public void weatherStationsJSONFileIsPresent() throws IOException {
        WeatherStationSource source = new WeatherStationsJSONFile(new Gson());
        WeatherStations stations = WeatherStations.loadFrom(source);
        assertThat(stations, is(not(empty())));
    }
    
    @Test
    public void fieldShouldContainFormattedInformation() {
        Field f = new Field("Test", "%.2f mm", 42.4242);
        assertThat(f.toString(), containsString("42.42 mm"));
    }
    
    @Test
    public void fieldShouldContainDateInformation() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Field f2 = new Field("Test2", format::format, new GregorianCalendar(2016, 1, 20).getTime());
        assertThat(f2.toString(), containsString("20"));
    }
}
