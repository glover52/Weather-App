package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.Favourites;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static org.chocvanilla.weatherapp.gui.MessageDialog.messageBox;

public class FavouritesManager extends WindowAdapter {
    private final Favourites favourites;

    public FavouritesManager(Favourites favourites) {
        this.favourites = favourites;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        try {
            favourites.saveToFile();
        } catch (IOException e) {
            messageBox("ERROR: Unable to save to favorites.", "ERROR");
            System.err.println("Error - unable to save favourites!");
            System.err.println(e.getMessage());
        }
    }
}
