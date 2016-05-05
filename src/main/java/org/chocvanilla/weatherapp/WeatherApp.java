package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.gui.*;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;

import java.io.IOException;

public class WeatherApp {
    public static void main(String[] args) {
        GuiHelpers.setLookAndFeel();
        Gson gson = new Gson();
        WeatherStationSource source = new WeatherStationsJSONFile(gson);
        try {
            WeatherStations stations = WeatherStations.loadFrom(source);
            MainWindow instance = new MainWindow(gson, stations);
            instance.run();
        } catch (IOException e) {
            MessageBox.showNow("ERROR: Weather stations file could not be loaded.", "ERROR!");
            System.exit(-1);
        }
    }
}
