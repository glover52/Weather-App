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

public class ChartHelpers {
    
    private ChartHelpers() {
        // static class
    }

    /**
     * Create a data set suitable for graphing using data obtained by loading all 
     * {@link WeatherObservation}s for the specified {@link WeatherStation}. 
     * @param observations the source of weather data for this data set
     * @return a data set containing the time series of weather observations
     */
    public static XYDataset createDataSet(List<WeatherObservation> observations) {
        TimeSeries series = new TimeSeries("Temperatures");
        for (WeatherObservation obs : observations) {
            series.addOrUpdate(new Second(obs.getTimestamp()), obs.getAirTemperature());
        }
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }


    /**
     * Download all available weather observations from the specified station.
     * @param station the source of the weather observations
     * @return a list of observations
     * @throws IOException if an error occurred while attempting the download
     */
    public static List<WeatherObservation> loadObservations(WeatherStation station) throws IOException {
        ObservationLoader loader = new ObservationLoader();
        return loader.load(station);
    }
    
    

    /**
     * Create a temperature chart which can be added to a graphical user interface.
     * @param station the weather station this chart is based on
     * @param observations the observations to display
     * @return a displayable {@link ChartPanel}
     */
    public static JFreeChart createChart(WeatherStation station, List<WeatherObservation> observations) {
        XYDataset dataset = createDataSet(observations);
        
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
        chart.setBackgroundPaint(null);
        return chart;
    }
}
