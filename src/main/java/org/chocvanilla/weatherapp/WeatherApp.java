package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.WeatherStations;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.chocvanilla.weatherapp.gui.MainWindow;

import javax.swing.*;

public class WeatherApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // ignore
        }
        MainWindow instance = new MainWindow(new WeatherStations(new WeatherStationsJSONFile()));
        instance.run();
    }
}
