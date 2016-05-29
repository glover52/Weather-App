package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.observations.WeatherObservation;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

public class ChartHelpers {

    private ChartHelpers() {
        // static class
    }

    /**
     * Create a data set suitable for graphing.
     *
     * @param observations the source of weather data for this data set
     * @return a data set containing the time series of weather observations
     */
    public static XYDataset createDataSet(WeatherObservations observations) {
        TimeSeries series = new TimeSeries("Air Temp");
        for (WeatherObservation obs : observations) {
            series.addOrUpdate(new Second(obs.getTimestamp()), obs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }

    /**
     * Create a temperature chart which can be added to a graphical user interface.
     *
     * @param station      the weather station this chart is based on
     * @param observations the observations to show
     * @return a displayable {@link ChartPanel}
     */
    public static JFreeChart createChart(WeatherStation station, WeatherObservations observations) {

        XYDataset dataset = createDataSet(observations);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.toString(),
                "Date", "", dataset);

        XYPlot plot = chart.getXYPlot();

        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);

        NumberAxis rangeAxis2 = new NumberAxis("Range Axis 2");
        rangeAxis2.setAutoRangeIncludesZero(false);

        GradientPaint g = new GradientPaint(
                0.0f, 0.0f, new Color(200, 210, 255),
                0.0f, 0.0f, new Color(255, 255, 255)
        );

        Color lineColor = new Color(150, 150, 150);
        plot.setBackgroundPaint(g);
        plot.setDomainGridlinePaint(lineColor);
        plot.setRangeGridlinePaint(lineColor);
        plot.setOutlineVisible(false);
        chart.setBackgroundPaint(null);

        return chart;
    }
}
