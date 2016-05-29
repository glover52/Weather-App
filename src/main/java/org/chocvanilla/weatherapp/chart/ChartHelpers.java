package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.observations.Field;
import org.chocvanilla.weatherapp.data.observations.WeatherObservation;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static TimeSeriesCollection createDataSets(WeatherObservations observations,
                                                      ArrayList<Field> fieldsToGraph) {
        TimeSeriesCollection seriesCollection = new TimeSeriesCollection();
        Map<Field, TimeSeries> seriesMap = new HashMap<>();
        for(Field field : fieldsToGraph) {
            TimeSeries series = new TimeSeries(field.getLabel());
            seriesMap.put(field, series);
        }


        for (WeatherObservation observation : observations) {
            List<Field> obsFields = observation.getFields();
            for(Field field : obsFields) {
                if(fieldsToGraph.contains(field)) {
                    seriesMap.put(field, updateSeries(field, seriesMap, observation));
                }
            }
        }

        for(Field field : fieldsToGraph) {
            seriesCollection.addSeries(seriesMap.get(field));
        }

        return seriesCollection;
    }

    private static TimeSeries updateSeries(Field field, Map<Field, TimeSeries> seriesMap,
                                           WeatherObservation observation) {
        TimeSeries series = seriesMap.get(field);
        series.addOrUpdate(new Second(observation.getTimestamp()),
                Integer.parseInt(field.getFormattedValue()));
        return series;
    }

    private static TimeSeriesCollection createTimeDataSet(Field field, WeatherObservations observations) {
        TimeSeries series = new TimeSeries(field.getLabel());
        for (WeatherObservation observation : observations) {
            for (Field obsField : observation.getFields()) {
                if(field == obsField) {
                    series.addOrUpdate(new Second(observation.getTimestamp()),
                            Integer.parseInt(obsField.getFormattedValue()));
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
    public static JFreeChart createChart(WeatherStation station, WeatherObservations observations,
                                         ArrayList<Field> fieldsToGraph) {

        XYDataset dataset = createDataSet(observations);
        //TimeSeriesCollection setOfFields = createDataSets(observations, fieldsToGraph);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.toString(),
                "Date", "Value", dataset);

        XYPlot plot = chart.getXYPlot();
        int dataSetIndex = 0;

        /*for (Field field : fieldsToGraph) {
            plot.setDataset(dataSetIndex, createTimeDataSet(field, observations));
            plot.setRenderer(dataSetIndex, new StandardXYItemRenderer());
            dataSetIndex++;
        }*/

        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);

        NumberAxis rangeAxis2 = new NumberAxis("Range Axis 2");
        rangeAxis2.setAutoRangeIncludesZero(false);

        /*XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
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
        chart.setBackgroundPaint(null);*/
        return chart;
    }
}
