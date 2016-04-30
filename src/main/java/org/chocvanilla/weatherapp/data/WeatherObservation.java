package org.chocvanilla.weatherapp.data;

import java.util.Date;

public interface WeatherObservation {

    Float getAirTemperature();

    Date getTimestamp();

    Float getApparentTemperature();

    Float getGustKm();

    Float getWindSpdKmh();

    String getWindDir();

    Float getRain();

    Float getDewPt();

    Float getGustKt();

    Float getWindSpdKt();
}
