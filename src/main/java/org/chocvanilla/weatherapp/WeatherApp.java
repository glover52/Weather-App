package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.Favourites;
import org.chocvanilla.weatherapp.data.WeatherStations;
import org.chocvanilla.weatherapp.gui.MainWindow;

import javax.swing.*;
import java.io.IOException;

public class WeatherApp {
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }
        WeatherStations all = WeatherStations.loadFromFile();
        Favourites favourites = Favourites.loadFromFile(all);
        MainWindow instance = new MainWindow(all, favourites);
        instance.run();
    }
}
