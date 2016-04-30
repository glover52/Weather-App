package org.chocvanilla.weatherapp.data;

import java.io.IOException;
import java.util.concurrent.FutureTask;

public interface WeatherStation {
    WeatherObservations load() throws IOException;
    FutureTask<WeatherObservations> loadAsync();

    String getName();

    String getState();

    boolean isFavourite();

    void setFavourite(boolean favourite);

    long msSinceLastRefresh();
}
