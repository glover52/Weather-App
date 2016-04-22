package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.chocvanilla.weatherapp.data.ObservationLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

import javax.swing.table.TableColumnModel;

import static org.chocvanilla.weatherapp.data.ObservationLoader.observationHistory;
import static org.chocvanilla.weatherapp.gui.GuiHelpers.fieldToLabel;

public class DetailWindow extends JFrame {
    private static final String REFRESH = "↻ Refresh";
    private static final String ADD_TO_FAVOURITES = "☆ Favourite";
    private static final String REMOVE_FROM_FAVOURITES = "★ Unfavourite";
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

    /**
     *  Create a new window, in which a chart with the most recent temperatures is displayed, both as a chart, and in a
     *  table. View is modelled in tabs.
     *
     * @param locationManager  saves previous window location, opens window to last location
     * @param listener  an interface for updating the favourites list instantly both on screen, and in program
     * @param favourites  the current working list of favourites
     */
    public DetailWindow(WindowLocationManager locationManager, FavouritesUpdatedListener listener,
                        Favourites favourites) {
        favouritesUpdatedListener = listener;
        this.favourites = favourites;

        detailFrame.setName("DetailWindow");
        detailFrame.addWindowListener(locationManager);
        JPanel container = new JPanel();
        detailFrame.setContentPane(container);
        detailFrame.setMinimumSize(new Dimension(700, 635));

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

    /**
     * The method that deals with the Java Swing context. All containers are cleared and populated with fresh data.
     * @param station  The {@link WeatherStation} for which data is displayed
     * @param dataSupplier  An asynchronous way of retireiving the {@link WeatherObservation} data, via
     *                     threading.
     */
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

    /**
     * Takes the {@link WeatherObservation} object, and creates and populates a JTable with the most recent data.
     * @param observations  The iterable list of weather observations.
     * @return the populated table, which is added to the appropriate JPanel.
     */
    private JTable buildTable(List<WeatherObservation> observations) {
        Object[][] data = observationHistory(observations);
        String[] columnNames = {"Time", "Air Temp", "Apparent Temp", "Gust (km/h)", "Gust (kt)",
                "Wind Direction", "Wind Speed (km/h)", "Wind Speed (kt)",
                "Dew Point", "Rain (mm)"};

        JTable table = new JTable(data, columnNames) {
            // Disable editing
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(230);

        return table;
    }

    /**
     * Create button to check if the current data is the most recent available.
     *
     * @param station the {@link WeatherStation} object, provides handle on object.
     * @return button which is then added to the correct JPanel
     */
    private JButton buildRefreshButton(WeatherStation station) {
        JButton refresh = new JButton(REFRESH);
        refresh.addActionListener(x -> display(station, ObservationLoader.loadAsync(station)));
        return refresh;
    }

    /**
     * Checks if chart has been initialised. Avoids Null Pointer Exceptions.
     *
     * @param chart the chart object, used for graphically displaying the data
     */
    private void updateChart(JFreeChart chart) {
        if (chartPanel == null) {
            chartPanel = new ChartPanel(chart);
            chartContainer.add(chartPanel);
        } else {
            chartPanel.setChart(chart);
        }
        setChartPanelAttributes(chartPanel);
    }

    /**
     * Method of disabling non-applicable functions from JFreeChart.
     *
     * @param chartPanel the chart equivalent of a JPanel, for use with JFrame.
     */
    private void setChartPanelAttributes(ChartPanel chartPanel) {
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setPopupMenu(null);
    }

    /**
     * Method of taking the most recent observations, and adding them to the returned JPanel
     *
     * @param observation the most recent observation.
     * @return New JPanel, added to DetailedWindow.
     */
    private JPanel buildDetails(WeatherObservation observation) {
        JPanel details = new JPanel();
        details.setLayout(new FlowLayout());
        for (ObservationDescription o : ObservationDescription.forObservation(observation)) {
            details.add(fieldToLabel(o.getLabel(), o.getValue()));
            details.add(new JSeparator(SwingConstants.VERTICAL));
        }
        return details;
    }

    /**
     * Method to create the button to add or remove from the favourites list. Dynamically creates the label for the
     * button depending on the stations existence in the favourites list.
     *
     * @param station handle on the station object being operated on
     * @param favourites the current working favourites list.
     * @return new JButton to add or remove from favourites.
     */
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

    /**
     * Method for toggling the favourites button between either Adding to Favourites, or Removing from Favourites.
     * Makes us of the {@link FavouritesUpdatedListener} to dynamically update the favourites list in the Main Window
     *
     * @param station handle on the station object being operated on.
     * @param button handle on the favourite button
     * @param favourites the current favourites list.
     */
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


