package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.*;
import org.chocvanilla.weatherapp.gui.MainWindow;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import static org.chocvanilla.weatherapp.chart.ChartHelpers.createChart;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeatherAppTest {
    private WeatherStations db;
    private ObservationLoader loader;
    private Favourites fav;

    /*
        Initialisation of some necessary functions to be run before any test. Populates a list of weather stations
        from the .weather_stations.json file, and loads any favourites stored to file.
     */
    @Before
    public void setUp() throws Exception {
        db = WeatherStations.loadFromFile();

        loader = new ObservationLoader();
        fav = Favourites.loadFromFile(db);
    }

    @Test
    public void weatherStationsAreLoaded() throws IOException {
        assertThat(db.getStations(), is(not(empty())));
    }

    @Test
    public void weatherObservationIsLoaded() throws IOException {
        WeatherStation station = db.getByWmoNumber(94828);
        List<WeatherObservation> observations = loader.load(station);
        assertThat(observations, is(not(empty())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadObservationFromNullStation() throws IOException {
        WeatherStation station = new WeatherStation();
        List<WeatherObservation> observations = loader.load(station);
    }

    @Test
    public void favouritesAreSaved() throws IOException {
        fav.add(db.getByWmoNumber(94828));
        fav.add(db.getByWmoNumber(94302));
        fav.add(db.getByWmoNumber(95607));
        fav.add(db.getByWmoNumber(94620));
        fav.add(db.getByWmoNumber(95625));
        fav.add(db.getByWmoNumber(94641));
        fav.saveToFile(); // fails on exception
    }

    @Test
    public void favouritesAreLoaded() throws IOException {
        favouritesAreSaved();

        assertTrue(fav.stream().anyMatch(x -> x.getWmoNumber() == 94828));
    }

    /*
        Testing of GUI elements
     */

    @Test
    public void makeWindow() {
        MainWindow frame = new MainWindow(db, fav);
        frame.run();
    }

    /*
        Testing of chart interfaces.
     */
    @Test
    public void canCreateChart() throws IOException {
        WeatherStation station = db.getByWmoNumber(94828);
        assertNotNull(station);
        List<WeatherObservation> observations = loader.load(station);
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
