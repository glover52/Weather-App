package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

import static org.chocvanilla.weatherapp.gui.GuiHelpers.fieldToLabel;

public class DetailWindow extends JFrame {
    private static final String ADD_TO_FAVOURITES = "Add to Favourites";
    private static final String REMOVE_FROM_FAVOURITES = "Remove from Favourites";
    private final Favourites favourites;
    private final FavouritesUpdatedListener favouritesUpdatedListener;
    private JFrame detailFrame = new JFrame();
    private JPanel latestObsContainer = new JPanel();
    private JPanel chartContainer = new JPanel();
    private JPanel tableContainer = new JPanel();
    private JPanel buttonContainer = new JPanel();
    private JTabbedPane historyContainer = new JTabbedPane();
    private ChartPanel chartPanel = null;
    private JLabel refreshStatusLabel = new JLabel();


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

        historyContainer.setBorder(BorderFactory.createTitledBorder("Observation History"));
        latestObsContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));

        historyContainer.addTab("Chart", chartContainer);
        historyContainer.addTab("Table", tableContainer);

        tableContainer.setLayout(new GridBagLayout());

        container.add(buttonContainer);
        container.add(latestObsContainer);
        container.add(historyContainer);
    }


    public void display(WeatherStation station, FutureTask<List<WeatherObservation>> dataSupplier) {
        try {
            List<WeatherObservation> observations = dataSupplier.get();

            long elapsed = ObservationLoader.msSinceLastRefresh(station);
            refreshStatusLabel.setText(String.format("Last refresh: %d seconds ago.",
                    TimeUnit.MILLISECONDS.toSeconds(elapsed)));

            Timer timer = new Timer(1000, x -> refreshStatusLabel.setText(""));
            timer.setRepeats(false);
            timer.start();

            latestObsContainer.removeAll();
            latestObsContainer.add(buildDetails(observations.get(0)));
            // Add to favorites
            buttonContainer.removeAll();
            buttonContainer.add(buildFavouritesButton(station, favourites));
            buttonContainer.add(buildRefreshButton(station));
            buttonContainer.add(refreshStatusLabel);

            // Need to revalidate to avoid artifacts from previous button
            buttonContainer.revalidate();
            buttonContainer.repaint();
            // Chart
            JFreeChart chart = ChartHelpers.createChart(station, observations);
            updateChart(chart);

            // Table
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 0;
            tableContainer.removeAll();
            tableContainer.add(new JScrollPane(buildTable(observations)), c);

            detailFrame.setTitle(station.getName());
            detailFrame.pack();
            detailFrame.setVisible(true);
        } catch (InterruptedException | ExecutionException ignored) {
            // Can't get data, so don't display chart
        }
    }

    private JTable buildTable(List<WeatherObservation> observations) {
        Object[][] data = ObservationHistory(observations);
        String[] columnNames = {"Time","Air Temp", "Apparent Temp", "Gust (km/h)", "Gust (kt)",
                "Wind Direction", "Wind Speed (km/h)", "Wind Speed (kt)",
                "Dew Point", "Rain (mm)"};

        JTable table = new JTable(data, columnNames){
            // Disable editing
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return table;
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

    private  Object[][] ObservationHistory(List<WeatherObservation> observations){
        Object[][] data = new Object[observations.size()][11];

        for (int i = 0; i < observations.size(); i++ ) {
            data[i][0] =  observations.get(i).getTimestamp();
            data[i][1] =  observations.get(i).getAirTemperature();
            data[i][2] =  observations.get(i).getApparentTemperature();
            data[i][3] =  observations.get(i).getGustKm();
            data[i][4] =  observations.get(i).getGustKt();
            data[i][5] =  observations.get(i).getWindDir();
            data[i][6] =  observations.get(i).getWindSpdKm();
            data[i][7] =  observations.get(i).getWindSpdKt();
            data[i][8] =  observations.get(i).getDewPt();
            data[i][9] =  observations.get(i).getRain();
            data[i][10] = observations.get(i).getTimestamp();
        }
        return data;
    }
}


