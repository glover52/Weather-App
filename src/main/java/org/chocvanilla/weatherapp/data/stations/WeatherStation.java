package org.chocvanilla.weatherapp.data.stations;

import org.chocvanilla.weatherapp.data.observations.WeatherObservations;

import java.util.concurrent.FutureTask;

public interface WeatherStation extends Comparable<WeatherStation> {
    
    WeatherObservations load();

    FutureTask<WeatherObservations> loadAsync();

    String getName();

    String getState();

    boolean isFavourite();

    void setFavourite(boolean favourite);

    long msSinceLastRefresh();
    
    String getUniqueID();

    double getLatitude();
    
    double getLongitude();
}
