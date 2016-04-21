package org.chocvanilla.weatherapp.gui;

import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.*;

/**
 * A {@link WindowAdapter} that saves the Window's last known location and dimensions
 * to a JSON file when the Window is closed, and restores them when it is re-opened.
 */
public class WindowLocationManager extends WindowAdapter {
    private static final String TARGET = ".preferences";
    private final Rectangle defaultBounds;
    private final Window parent;

    /**
     * Initialises a new WindowLocation manager with the specified values.
     *
     * @param defaultBounds the initial size and location of the Window
     * @param parent        the parent window
     */
    public WindowLocationManager(Rectangle defaultBounds, Window parent) {
        this.defaultBounds = defaultBounds;
        this.parent = parent;
    }

    private static Path getPathFor(Window window) {
        return Paths.get(TARGET, window.getName() + ".json");
    }

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
        try (BufferedWriter writer = Files.newBufferedWriter(getPathFor(window))) {
            Rectangle bounds = window.getBounds();
            Gson gson = new Gson();
            gson.toJson(bounds, writer);
        } catch (IOException e) {
            // failed to save coordinates
        }
    }

    private void loadCoordinates(Window window) {
        Paths.get(TARGET).toFile().mkdirs();
        try (BufferedReader reader = Files.newBufferedReader(getPathFor(window))) {
            Gson gson = new Gson();
            Rectangle bounds = gson.fromJson(reader, Rectangle.class);
            window.setBounds(bounds);
        } catch (IOException ignored) {
            if (defaultBounds != null) {
                window.setBounds(defaultBounds);
            }
            window.setLocationRelativeTo(parent);
        }
    }

}
