package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.WeatherStations;
import org.chocvanilla.weatherapp.gui.MainWindow;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;

import javax.swing.*;

public class WeatherApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // ignore
        }
        Gson gson = new Gson();
        WeatherStationsJSONFile file = new WeatherStationsJSONFile(gson);
        WeatherStations stations = new WeatherStations(file);
        MainWindow instance = new MainWindow(stations);
        instance.run();
    }
}
