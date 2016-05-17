package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FavouritesManager extends WindowAdapter {
    private final WeatherStations allStations;

    public FavouritesManager(WeatherStations allStations) {
        this.allStations = allStations;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        allStations.getFavourites().forEach(WeatherStation::loadAsync);
    }

    @Override
    public void windowClosing(WindowEvent event) {
        allStations.save();
    }
}
