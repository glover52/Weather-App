package org.chocvanilla.weatherapp.data;


public class BomWeatherStation {
    private static final String URL_FORMAT = "http://www.bom.gov.au/fwo/%s/%s.%d.json";
    private static final String PRODUCT_ID = "ID%c60801";

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

    private String formatProductID() {
        return String.format(PRODUCT_ID, getCode());
    }

    public String getUrl() {
        String productID = formatProductID();
        return String.format(URL_FORMAT, productID, productID, wmoNumber);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, state);
    }
}
