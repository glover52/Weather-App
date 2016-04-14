package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.*;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Chart {
    public Chart(WeatherStation station, XYDataset dataset) {
        JFrame frame = new JFrame(station.getName());
        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.getName(),
                            "Date", "Degrees Celsius", dataset);
        ChartPanel cp = new ChartPanel(chart);
        frame.getContentPane().add(cp);
        frame.setVisible(true);
        frame.pack();
    }

    public static XYDataset createDataSet(WeatherStation station) throws IOException {
        TimeSeries series = new TimeSeries("Temperatures");
        ObservationLoader loader = new ObservationLoader();
        List<WeatherObservation> observations = loader.load(station);
        for(WeatherObservation obvs : observations) {
            String timeStamp = obvs.getTimestamp();
            int year = Integer.parseInt(timeStamp.substring(0,4));
            int month = Integer.parseInt(timeStamp.substring(4,6));
            int day = Integer.parseInt(timeStamp.substring(6,8));
            int hour = Integer.parseInt(timeStamp.substring(8,10));
            int minute = Integer.parseInt(timeStamp.substring(10, 12));
            series.addOrUpdate(new Minute(minute, hour, day, month, year), obvs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }
}
