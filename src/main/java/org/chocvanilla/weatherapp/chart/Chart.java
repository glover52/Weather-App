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
        for(WeatherObservation obs : observations) {
            series.addOrUpdate(new Second(obs.getTimestamp()), obs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }
}
