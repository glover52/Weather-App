package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.Favourites;
import org.chocvanilla.weatherapp.data.WeatherStations;
import org.chocvanilla.weatherapp.gui.MainWindow;

import javax.swing.*;

public class WeatherApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // ignore
        }
        WeatherStations all = WeatherStations.loadFromFile();
        Favourites favourites = Favourites.loadFromFile(all);
        MainWindow instance = new MainWindow(all, favourites);
        instance.run();
    }
}
