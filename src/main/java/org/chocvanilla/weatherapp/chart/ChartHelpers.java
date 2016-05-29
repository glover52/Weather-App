package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.observations.*;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
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
        return createDataSet(new Field("Air Temp", "", ""), observations);
    }

    public static XYDataset createDataSet(Field field, WeatherObservations observations) {
        TimeSeries series = new TimeSeries(field.getLabel());
        for (WeatherObservation observation : observations) {
            for (Field obsField : observation.getFields()) {
                if (field.isGraphable() && field.getLabel().equals(obsField.getLabel())) {
                    series.addOrUpdate(new Second(observation.getTimestamp()),
                            Double.parseDouble(obsField.getValue().toString()));
                    break;
                }
            }
        }

        return new TimeSeriesCollection(series);
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

        enableGridlines(plot);
        chart.setBackgroundPaint(null);

        return chart;
    }

    public static void enableGridlines(XYPlot plot) {
        GradientPaint g = new GradientPaint(
                0.0f, 0.0f, new Color(200, 210, 255),
                0.0f, 0.0f, new Color(255, 255, 255)
        );

        Color lineColor = new Color(150, 150, 150);
        plot.setBackgroundPaint(g);
        plot.setDomainGridlinePaint(lineColor);
        plot.setRangeGridlinePaint(lineColor);
        plot.setOutlineVisible(false);
    }


}
