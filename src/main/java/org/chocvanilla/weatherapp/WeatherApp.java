package org.chocvanilla.weatherapp;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.observations.BureauOfMeteorology;
import org.chocvanilla.weatherapp.data.observations.ObservationsProvider;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.gui.MainWindow;
import org.chocvanilla.weatherapp.gui.MessageBox;
import org.chocvanilla.weatherapp.io.WeatherStationSource;
import org.chocvanilla.weatherapp.io.WeatherStationsJSONFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;

public class WeatherApp {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void run() {
        log.debug("Weather-App started");
        setLookAndFeel();
        Gson gson = new Gson();
        WeatherStationSource source = new WeatherStationsJSONFile(gson);
        ObservationsProvider provider = new BureauOfMeteorology();
        try {
            WeatherStations stations = WeatherStations.loadFrom(source);
            MainWindow window = new MainWindow(gson, stations, provider);
            window.show();
        } catch (IOException e) {
            String message = "Weather stations file could not be loaded.";
            MessageBox.showNow(message, "ERROR!");
            log.error(message, e);
            System.exit(-1);
        }        
    }

    public void setLookAndFeel() {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName(); 
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            log.warn("Failed to set Look and Feel to '{}'", lookAndFeel, e);
        }
    }
    
    public static void main(String[] args) {
        new WeatherApp().run();
    }
}
