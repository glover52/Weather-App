package org.chocvanilla.weatherapp.io;

import java.io.IOException;

public class KeyProvider {
    private static String key = "52630e6a72bebe703e82148de11ec407";

    public static String getForecastAPIKey() throws MissingAPIKeyException {
        try {
            if (key == null) {
                key = FileSystemHelpers.getResource(KeyProvider.class, "/forecast_api_key").readLine();
            }
            return key;
        } catch (IOException e) {
            throw new MissingAPIKeyException("ForecastIO API key missing, go to https://developer.forecast.io/register");
        }
    }
}
