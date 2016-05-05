package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.observations.WeatherObservation;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
        TimeSeries series = new TimeSeries("Temperature");
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
     * @param observations the observations to display
     * @return a displayable {@link ChartPanel}
     */
    public static JFreeChart createChart(WeatherStation station, WeatherObservations observations) {
        XYDataset dataset = createDataSet(observations);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.getName() + ", " + station.getState(),
                "Date", "Degrees Celsius", dataset);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 0));
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDataset(0, dataset);
        plot.setRenderer(0, renderer);

        GradientPaint g = new GradientPaint(
                0.0f, 0.0f, new Color(182, 212, 255),
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

    /**
     * Transforms a list of observations into a multidimensional array of objects
     *
     * @param observations A list of weather observations
     * @return an array of observations
     */
    public static Object[][] observationHistory(WeatherObservations observations) {
        Object[][] data = new Object[observations.size()][];
        int i = 0;
        for (WeatherObservation o : observations) {
            data[i++] = new Object[]{
                    o.getTimestamp(),
                    o.getAirTemperature(),
                    o.getApparentTemperature(),
                    o.getGustKm(),
                    o.getGustKt(),
                    o.getWindDir(),
                    o.getWindSpdKmh(),
                    o.getWindSpdKt(),
                    o.getDewPt(),
                    o.getRain(),
            };
        }
        return data;
    }


}
