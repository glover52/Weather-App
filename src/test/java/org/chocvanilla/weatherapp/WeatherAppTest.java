package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeatherAppTest {
    private WeatherStations db;
    private ObservationLoader loader;
    private Favourites fav;

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

    @Test
    public void favouritesAreSaved() throws IOException {
        fav.add(db.getByWmoNumber(94828));
        fav.add(db.getByWmoNumber(94302));
        fav.add(db.getByWmoNumber(95607));
        fav.add(db.getByWmoNumber(94620));
        fav.add(db.getByWmoNumber(95625));
        fav.add(db.getByWmoNumber(94641));
        assertTrue(fav.saveToFile());
    }

    @Test
    public void favouritesAreLoaded() throws IOException {
        favouritesAreSaved();

        assertTrue(fav.stream().anyMatch(x -> x.getWmoNumber() == 94828));
    }

    @Test
    public void displayObservations() throws IOException {
        WeatherStation station = db.getByWmoNumber(94828);

        List<WeatherObservation> observations = loader.load(station);

        observations.forEach(System.out::println);
    }

}
