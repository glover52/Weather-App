package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.observations.ObservationsProvider;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FavouritesManager extends WindowAdapter {
    private final WeatherStations allStations;
    private final ObservationsProvider provider;

    public FavouritesManager(WeatherStations allStations, ObservationsProvider provider) {
        this.allStations = allStations;
        this.provider = provider;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        allStations.getFavourites().forEach(provider::loadObservations);
    }

    @Override
    public void windowClosing(WindowEvent event) {
        allStations.save();
    }
}
