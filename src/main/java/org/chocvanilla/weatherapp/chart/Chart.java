package org.chocvanilla.weatherapp.chart;

import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Chart {
    public Chart() {
    }

    public static XYDataset createDataSet(WeatherStation station) throws IOException {
        TimeSeries series = new TimeSeries("Temperatures");
        ObservationLoader loader = new ObservationLoader();
        List<WeatherObservation> observations = loader.load(station);
        for (WeatherObservation obs : observations) {
            series.addOrUpdate(new Second(obs.getTimestamp()), obs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }

    public ChartPanel createChart(WeatherStation station, XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(station.getName(),
                "Date", "Degrees Celsius", dataset);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 255));
        renderer.setSeriesShapesVisible(0, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDataset(0, dataset);
        plot.setRenderer(0, renderer);

        GradientPaint g = new GradientPaint(
                0.0f, 0.0f, new Color(83, 164, 225),
                0.0f, 0.0f, new Color(255, 70, 70)
        );
        plot.setBackgroundPaint(g);

        plot.setOutlineVisible(false);

        return new ChartPanel(chart);
    }
}
