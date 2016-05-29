package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.observations.ObservationsProvider;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.io.AsyncLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class FavouritesManager extends WindowAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final WeatherStations allStations;
    private final ObservationsProvider provider;

    public FavouritesManager(WeatherStations allStations, ObservationsProvider provider) {
        this.allStations = allStations;
        this.provider = provider;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        allStations.getFavourites().forEach(
                station -> new AsyncLoader(station).loadAsync(provider::loadObservations));
    }

    @Override
    public void windowClosing(WindowEvent event) {
        try {
            allStations.save();
        } catch (IOException e) {
            String message = "Favourites file could not be saved!";
            MessageBox.show(message, "ERROR!");
            log.error(message, e);
        }
    }
}
