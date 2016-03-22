package org.chocvanilla.weatherapp;

import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class AppListener extends WindowAdapter {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final String PREFERENCES_PATH = "preferences.json";

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        saveCoordinates(e.getWindow());
    }

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        loadCoordinates(e.getWindow());
    }


    private void saveCoordinates(Window window) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PREFERENCES_PATH))){
            Rectangle bounds = window.getBounds();
            Gson gson = new Gson();
            gson.toJson(bounds, writer);
        } catch (IOException e) {
            // failed to save coordinates
        }
    }
    
    private void loadCoordinates(Window window) {
        try  (BufferedReader reader = new BufferedReader(new FileReader(PREFERENCES_PATH))){
            Gson gson = new Gson();
            Rectangle bounds = gson.fromJson(reader, Rectangle.class);
            window.setBounds(bounds);           
        } catch (IOException ignored){
            window.setSize(DEFAULT_WINDOW_SIZE);
            window.setLocationRelativeTo(null);
        }
 
    }

}
