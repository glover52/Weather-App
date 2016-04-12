package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.data.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WeatherApp {
    final JFrame mainWindow = new JFrame("Weather App");
    private final JList<String> favouritesList = new JList<>();
    private final WeatherStations weatherStations;
    private final Favourites favourites;
    
    public WeatherApp(WeatherStations stations){
        weatherStations = stations;
        favourites = new Favourites(weatherStations);
        
    }
    
    public void run() {
        
        favourites.loadFromFile();
        
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.addWindowListener(new AppListener());
        
        DefaultListModel<String> model = new DefaultListModel<>();
        for (WeatherStation fav : favourites){
            model.addElement(fav.getName() + " " + fav.getState());
        }
        favouritesList.setModel(model);
        
        JPanel container = new JPanel(new FlowLayout());
        container.add(favouritesList);
        mainWindow.setContentPane(container);
        
        
        mainWindow.pack();
        mainWindow.setVisible(true);
        
    }
    
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //ignore
        }
        WeatherStations all = new WeatherStations();
        all.load();
        WeatherApp instance = new WeatherApp(all);
        instance.run();
    }
}
