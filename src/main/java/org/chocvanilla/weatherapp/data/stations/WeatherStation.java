package org.chocvanilla.weatherapp.data.stations;

public interface WeatherStation extends Comparable<WeatherStation> {

    String getName();

    String getState();

    boolean isFavourite();

    void setFavourite(boolean favourite);

    String getUniqueID();

    double getLatitude();

    double getLongitude();
}
