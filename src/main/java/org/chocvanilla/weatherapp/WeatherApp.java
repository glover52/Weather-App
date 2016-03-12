package org.chocvanilla.weatherapp;

import javax.swing.*;

public class WeatherApp {
    private final JFrame mainWindow = new JFrame("Weather App");
    
    public void run() {
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setSize(800, 600);
        mainWindow.setLocationRelativeTo(null);
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
