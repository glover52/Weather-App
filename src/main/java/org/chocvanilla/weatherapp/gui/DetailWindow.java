package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.WeatherObservation;
import org.chocvanilla.weatherapp.data.WeatherStation;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.chocvanilla.weatherapp.gui.GuiHelpers.fieldToLabel;

public class DetailWindow extends JFrame {
    private JFrame detailFrame = new JFrame();
    private JPanel latestObsContainer = new JPanel();
    private JPanel chartContainer = new JPanel();
    private ChartPanel chartPanel = null;


    public DetailWindow(WindowLocationManager locationManager) {
        detailFrame.setName("DetailWindow");
        detailFrame.addWindowListener(locationManager);
        JPanel container = new JPanel();
        detailFrame.setContentPane(container);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        chartContainer.setBorder(BorderFactory.createTitledBorder("Temperature History"));
        latestObsContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));

        container.add(chartContainer);
        container.add(latestObsContainer);
    }


    public void display(WeatherStation station, FutureTask<List<WeatherObservation>> dataSupplier) {
        try {
            List<WeatherObservation> observations = dataSupplier.get();
            latestObsContainer.removeAll();
            latestObsContainer.add(buildDetails(observations.get(0)));
            JFreeChart chart = ChartHelpers.createChart(station, observations);
            updateChart(chart);
            detailFrame.setTitle(station.getName());
            detailFrame.pack();
            detailFrame.setVisible(true);
        } catch (InterruptedException | ExecutionException ignored) {
            // Can't get data, so don't display chart
        }
    }

    private void updateChart(JFreeChart chart) {
        if (chartPanel == null) {
            chartPanel = new ChartPanel(chart);
            chartContainer.add(chartPanel);
        } else {
            chartPanel.setChart(chart);
        }
    }

    private JPanel buildDetails(WeatherObservation observation) {
        JPanel details = new JPanel();
        details.setLayout(new FlowLayout());
        for (ObservationDescription o : ObservationDescription.forObservation(observation)){
            details.add(fieldToLabel(o.getLabel(), o.getValue()));
            details.add(new JSeparator(SwingConstants.VERTICAL));
        }
        return details;
    }
}


