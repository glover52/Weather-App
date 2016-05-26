package org.chocvanilla.weatherapp.io;

import java.io.IOException;

public class KeyProvider {
    private static String key;
    
    public static String getForecastAPIKey() {
        try {
            if (key == null) {
                key = FileSystemHelpers.getResource(KeyProvider.class, "/forecast_api_key").readLine();
            }
            return key;
        } catch (IOException e) {
            return ""; // todo: let user know that api key is missing
        }
    }
}
