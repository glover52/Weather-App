package org.chocvanilla.weatherapp;

import javax.swing.*;
import org.chocvanilla.weatherapp.gui.*;

public class WeatherApp {
    final JFrame mainWindow = new JFrame("Weather App");
    
    public void run() {
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.addWindowListener(new AppListener());
        mainWindow.setVisible(true);
    }
    
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //ignore
        }
        WeatherApp instance = new WeatherApp();
        instance.run();
    }
}
