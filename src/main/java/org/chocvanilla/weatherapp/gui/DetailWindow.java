package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.Favourites;
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
    private JPanel buttonContainer = new JPanel();
    private ChartPanel chartPanel = null;
    private final FavouritesUpdatedListener favouritesUpdatedListener;

    public DetailWindow(WindowLocationManager locationManager, FavouritesUpdatedListener listener) {
        favouritesUpdatedListener = listener;
        
        detailFrame.setName("DetailWindow");
        detailFrame.addWindowListener(locationManager);
        JPanel container = new JPanel();
        detailFrame.setContentPane(container);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        buttonContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        chartContainer.setBorder(BorderFactory.createTitledBorder("Temperature History"));
        latestObsContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));

        container.add(buttonContainer);
        container.add(chartContainer);
        container.add(latestObsContainer);
    }


    public void display(WeatherStation station, FutureTask<List<WeatherObservation>> dataSupplier,
                        Favourites favourites) {
        try {
            List<WeatherObservation> observations = dataSupplier.get();
            latestObsContainer.removeAll();
            latestObsContainer.add(buildDetails(observations.get(0)));
            buttonContainer.removeAll();
            buttonContainer.add(addFavouriteButton(station, favourites));
            
            // Need to revalidate to avoid artifacts from previous button
            buttonContainer.revalidate();
            buttonContainer.repaint();
            
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

    private JButton addFavouriteButton(WeatherStation station, Favourites favourites) {
        JButton addRemoveFavourite = new JButton();
        if (favourites.contains(station)) {
            addRemoveFavourite.setText("Remove from Favourites");
        } else {
            addRemoveFavourite.setText("Add to Favourites");
        }
        addRemoveFavourite.addActionListener(x -> toggleFavourites(station, addRemoveFavourite, favourites));
        return addRemoveFavourite;
    }

    private void toggleFavourites(WeatherStation station, JButton button, Favourites favourites){
        if (favourites.contains(station)) {
            favourites.remove(station);
            button.setText("Add to Favourites");
        }
        else {
            button.setText("Remove from Favourites");
            favourites.add(station);
        }
        favouritesUpdatedListener.update();
    }
}


