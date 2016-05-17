package org.chocvanilla.weatherapp.data.observations;

import java.util.Date;
import java.util.List;

public interface WeatherObservation {
    Date getTimestamp();
    double getAirTemperature();
    List<Field> getFields();
}
