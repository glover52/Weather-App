package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.ObservationLoader;
import org.chocvanilla.weatherapp.data.WeatherObservation;
import org.chocvanilla.weatherapp.data.WeatherStation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Chart {
    public Chart(WeatherStation station) throws IOException {
        JFrame frame = new JFrame(station.getName());

        frame.setSize(600,400);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.getName(),
                            "Date", "Degrees Celsius", createDataSet(station));

        ChartPanel cp = new ChartPanel(chart);
        frame.getContentPane().add(cp);

        frame.setVisible(true);
    }

    private XYDataset createDataSet(WeatherStation station) throws IOException {
        TimeSeries series = new TimeSeries("Temperatures");
        ObservationLoader loader = new ObservationLoader();
        List<WeatherObservation> observations = loader.load(station);
        for(WeatherObservation obvs: observations) {
            String timeStamp = obvs.getTimestamp();
            int year = Integer.parseInt(timeStamp.substring(0,4));
            int month = Integer.parseInt(timeStamp.substring(4,6));
            int day = Integer.parseInt(timeStamp.substring(6,8));
            int hour = Integer.parseInt(timeStamp.substring(8,12));
            series.addOrUpdate(new Hour(hour, day, month, year), obvs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }
}
