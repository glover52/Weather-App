package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class WeatherAppTest {
    private WeatherStations db;
    private ObservationLoader loader;

    @Before
    public void setUp() throws Exception {
        db = new WeatherStations();
        db.load();
        loader = new ObservationLoader();
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
        Favourites fav = new Favourites(db);
        fav.addToFavourites(db.getByWmoNumber(94828));
        fav.addToFavourites(db.getByWmoNumber(94302));
        fav.addToFavourites(db.getByWmoNumber(95607));
        fav.addToFavourites(db.getByWmoNumber(94620));
        fav.addToFavourites(db.getByWmoNumber(95625));
        fav.addToFavourites(db.getByWmoNumber(94641));
        assertTrue(fav.saveToFile());
    }

    @Test
    public void favouritesAreLoaded() throws IOException {
        Favourites fav = new Favourites(db);
        assertTrue(fav.loadFromFile());
        WeatherStation station = fav.getFavourite(0);
        assertEquals(station.getWmoNumber(), 94828);
    }

    @Test
    public void displayObservations() throws IOException {
        Favourites fav = new Favourites(db);
        assertTrue(fav.loadFromFile());
        WeatherStation station = fav.getFavourite(0);
        List<WeatherObservation> observations = loader.load(station);
        for(WeatherObservation obvs : observations) {
            Date time = obvs.getTimestamp();
            double temp = obvs.getAirTemperature();
            System.out.println(time + ":" + temp);
        }
    }

}
