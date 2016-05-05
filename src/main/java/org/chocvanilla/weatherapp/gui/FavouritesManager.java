package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.stations.WeatherStations;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FavouritesManager extends WindowAdapter {
    private final WeatherStations favourites;

    public FavouritesManager(WeatherStations favourites) {
        this.favourites = favourites;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        favourites.save();
    }
}
