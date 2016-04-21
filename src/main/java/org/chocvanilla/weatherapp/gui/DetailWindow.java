package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.chocvanilla.weatherapp.gui.GuiHelpers.fieldToLabel;

public class DetailWindow extends JFrame {
    private static final String ADD_TO_FAVOURITES = "Add to Favourites";
    private static final String REMOVE_FROM_FAVOURITES = "Remove from Favourites";
    private final Favourites favourites;
    private final FavouritesUpdatedListener favouritesUpdatedListener;
    private JFrame detailFrame = new JFrame();
    private JPanel latestObsContainer = new JPanel();
    private JPanel chartContainer = new JPanel();
    private JPanel buttonContainer = new JPanel();
    private ChartPanel chartPanel = null;


    public DetailWindow(WindowLocationManager locationManager, FavouritesUpdatedListener listener,
                        Favourites favourites) {
        favouritesUpdatedListener = listener;
        this.favourites = favourites;

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


    public void display(WeatherStation station, FutureTask<List<WeatherObservation>> dataSupplier) {
        try {
            List<WeatherObservation> observations = dataSupplier.get();
            latestObsContainer.removeAll();
            latestObsContainer.add(buildDetails(observations.get(0)));
            buttonContainer.removeAll();
            buttonContainer.add(buildFavouritesButton(station, favourites));
            buttonContainer.add(buildRefreshButton(station));

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

    private JButton buildRefreshButton(WeatherStation station) {
        JButton refresh = new JButton("↻");
        refresh.addActionListener(x -> display(station, ObservationLoader.loadAsync(station)));
        return refresh;
    }

    private void updateChart(JFreeChart chart) {
        if (chartPanel == null) {
            chartPanel = new ChartPanel(chart);
            chartContainer.add(chartPanel);
        } else {
            chartPanel.setChart(chart);
        }
        setChartPanelAttributes(chartPanel);
    }

    private void setChartPanelAttributes(ChartPanel chartPanel) {
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);
    }

    private JPanel buildDetails(WeatherObservation observation) {
        JPanel details = new JPanel();
        details.setLayout(new FlowLayout());
        for (ObservationDescription o : ObservationDescription.forObservation(observation)) {
            details.add(fieldToLabel(o.getLabel(), o.getValue()));
            details.add(new JSeparator(SwingConstants.VERTICAL));
        }
        return details;
    }

    private JButton buildFavouritesButton(WeatherStation station, Favourites favourites) {
        JButton addRemoveFavourite = new JButton();
        if (favourites.contains(station)) {
            addRemoveFavourite.setText(REMOVE_FROM_FAVOURITES);
        } else {
            addRemoveFavourite.setText(ADD_TO_FAVOURITES);
        }
        addRemoveFavourite.addActionListener(x -> toggleFavourites(station, addRemoveFavourite, favourites));
        return addRemoveFavourite;
    }

    private void toggleFavourites(WeatherStation station, JButton button, Favourites favourites) {
        if (favourites.contains(station)) {
            favourites.remove(station);
            button.setText(ADD_TO_FAVOURITES);
        } else {
            button.setText(REMOVE_FROM_FAVOURITES);
            favourites.add(station);
        }
        favouritesUpdatedListener.update();
    }
}


