package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.WeatherStations;
import org.chocvanilla.weatherapp.gui.GuiHelpers;
import org.chocvanilla.weatherapp.gui.MainWindow;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;

public class WeatherApp {
    public static void main(String[] args) {
        GuiHelpers.setLookAndFeel();
        Gson gson = new Gson();
        WeatherStationsJSONFile file = new WeatherStationsJSONFile(gson);
        WeatherStations stations = new WeatherStations(file);
        MainWindow instance = new MainWindow(gson, stations);
        instance.run();
    }
}
