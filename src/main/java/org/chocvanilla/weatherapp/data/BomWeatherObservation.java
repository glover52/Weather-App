package org.chocvanilla.weatherapp.data;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BomWeatherObservation implements WeatherObservation {
    @SerializedName("local_date_time_full")
    private String timestamp;

    @SerializedName("name")
    private String name;

    @SerializedName("apparent_t")
    private Float apparentTemperature;

    @SerializedName("gust_kmh")
    private Float gustKm;

    @SerializedName("gust_kt")
    private Float gustKt;

    @SerializedName("air_temp")
    private Float airTemperature;

    @SerializedName("dewpt")
    private Float dewPt;

    @SerializedName("rain_trace")
    private Float rain;

    @SerializedName("wind_dir")
    private String windDir;

    @SerializedName("wind_spd_kmh")
    private Float windSpdKm;

    @SerializedName("wind_spd_kt")
    private float windSpdKt;

    public Object[] fieldsAsTableRow() {
        Object[] data = new Object[10];
        data[0] = getTimestamp();
        data[1] = getAirTemperature();
        data[2] = getApparentTemperature();
        data[3] = getGustKm();
        data[4] = getGustKt();
        data[5] = getWindDir();
        data[6] = getWindSpdKm();
        data[7] = getWindSpdKt();
        data[8] = getDewPt();
        data[9] = getRain();
        return data;
    }

    public Date getTimestamp() {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public Float getApparentTemperature() {
        return apparentTemperature;
    }

    public Float getGustKm() {
        return gustKm;
    }

    public Float getGustKt() {
        return gustKt;
    }

    public Float getDewPt() {
        return dewPt;
    }

    public Float getRain() {
        return rain;
    }

    public String getWindDir() {
        return windDir;
    }

    public Float getWindSpdKm() {
        return windSpdKm;
    }

    public Float getWindSpdKt() {
        return windSpdKt;
    }

    public Float getAirTemperature() {
        return airTemperature;
    }

    @Override
    public String toString() {
        return String.format("WeatherObservation{timestamp='%s', name='%s', " +
                        "apparentTemperature=%s, gustKm=%s, gustKt=%s, airTemperature=%s, " +
                        "dewPt=%s, rain=%s, windDir='%s', windSpdKm=%s, windSpdKt=%s}",
                timestamp, name, apparentTemperature, gustKm, gustKt,
                airTemperature, dewPt, rain, windDir, windSpdKm, windSpdKt);
    }
}

