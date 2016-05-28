package org.chocvanilla.weatherapp.data.stations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// fields are auto-set by Gson
@SuppressWarnings("unused") 
public class BomWeatherStation implements WeatherStation {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private int stationID;
    private String wmoNumber;
    private String name;
    private String state;
    private double latitude;
    private double longitude;


    private boolean favourite;


    @Override
    public int compareTo(WeatherStation two) {
        int result = getState().compareTo(two.getState());
        if (result != 0) {
            return result;
        }
        return getName().compareTo(two.getName());
    }


    public int getStationID() {
        return stationID;
    }

    public String getWmoNumber() {
        return wmoNumber;
    }


    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, state);
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String getUniqueID() {
        return wmoNumber;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }
}
