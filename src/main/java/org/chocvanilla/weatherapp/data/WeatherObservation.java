package org.chocvanilla.weatherapp.data;

import java.util.Date;

public interface WeatherObservation {

    Object[] fieldsAsTableRow();

    Float getAirTemperature();

    Date getTimestamp();

    Float getApparentTemperature();

    Float getGustKm();

    Float getWindSpdKm();

    String getWindDir();

    Float getRain();

    Float getDewPt();
}
