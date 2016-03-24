package org.chocvanilla.weatherapp.data;

public class WeatherStation {
    private int stationID;
    private int wmoNumber;
    private String name;
    private String state;
    private char code;

    public int getStationID() {
        return stationID;
    }
    
    public int getWmoNumber() {
        return wmoNumber;
    }

    
    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public char getCode() {
        return code;
    }
    
}
