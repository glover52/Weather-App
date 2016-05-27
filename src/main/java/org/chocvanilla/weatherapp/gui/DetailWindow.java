package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.observations.*;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.io.AsyncLoader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class DetailWindow {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String REFRESH = "↻ Refresh";
    private static final String ADD_TO_FAVOURITES = "☆ Favourite";
    private static final String REMOVE_FROM_FAVOURITES = "★ Unfavourite";
    private final FavouritesUpdatedListener favouritesUpdatedListener;
    private final JFrame detailFrame = new JFrame();
    private final JPanel latestObsContainer = new JPanel();
    private final JPanel chartContainer = new JPanel();
    private final JPanel checkBoxContainer = new JPanel();
    private final JPanel tableContainer = new JPanel();
    private final JPanel buttonContainer = new JPanel();
    private final JTabbedPane historyContainer = new JTabbedPane();
    private ChartPanel chartPanel = null;
    private final JLabel refreshStatusLabel = new JLabel();
    private ArrayList<Field> fieldsToGraph = new ArrayList<>();

    /**
     * Create a new window, in which a chart with the most recent temperatures is displayed, both as a chart, and in a
     * table. View is modelled in tabs.
     *
     * @param locationManager saves previous window location, opens window to last location
     * @param listener        an interface for updating the favourites list instantly both on screen, and in program
     */
    public DetailWindow(WindowLocationManager locationManager, FavouritesUpdatedListener listener) {
        favouritesUpdatedListener = listener;

        detailFrame.setName("DetailWindow");
        log.debug("{} created", detailFrame.getName());
        detailFrame.addWindowListener(locationManager);
        JPanel container = new JPanel();
        detailFrame.setContentPane(container);
        detailFrame.setMinimumSize(new Dimension(700, 710));

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        buttonContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        historyContainer.setBorder(BorderFactory.createTitledBorder("Observation History"));
        latestObsContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));

        historyContainer.addTab("Chart", chartContainer);
        historyContainer.addTab("Table", tableContainer);

        tableContainer.setLayout(new GridBagLayout());

        checkBoxContainer.setLayout(new FlowLayout());
        checkBoxContainer.setMinimumSize(new Dimension(700, 100));

        container.add(buttonContainer);
        container.add(latestObsContainer);
        container.add(historyContainer);
    }

    /**
     * The method that deals with the Java Swing context. All containers are cleared and populated with fresh data.
     *
     * @param station      The {@link WeatherStation} for which data is displayed
     * @param dataSupplier An asynchronous way of retireiving the {@link WeatherObservation} data, via
     */
    public void show(WeatherStation station, FutureTask<WeatherObservations> dataSupplier) {
        try {
            WeatherObservations observations = dataSupplier.get();
            updateDataFrom(station, observations);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to load observations from '{}'", station.getName(), e);
        }
    }

    private void updateDataFrom(WeatherStation station, WeatherObservations observations) {
//        long elapsed = station.msSinceLastRefresh();
//        refreshStatusLabel.setText(String.format("Last refresh: %d seconds ago.",
//                TimeUnit.MILLISECONDS.toSeconds(elapsed)));
//
//        Timer timer = new Timer(1000, x -> refreshStatusLabel.setText(""));
//        timer.setRepeats(false);
//        timer.start();

        latestObsContainer.removeAll();
        latestObsContainer.add(GuiHelpers.buildDetails(observations.iterator().next()));
        // Add to favorites
        buttonContainer.removeAll();
        buttonContainer.add(buildFavouritesButton(station));
        buttonContainer.add(buildRefreshButton(station));
        buttonContainer.add(refreshStatusLabel);

        // Need to revalidate to avoid artifacts from previous button
        buttonContainer.revalidate();
        buttonContainer.repaint();
        // Chart
        JFreeChart chart = ChartHelpers.createChart(station, observations, fieldsToGraph);
        updateChart(chart);

        addCheckBoxes(observations);
        checkBoxContainer.revalidate();
        checkBoxContainer.repaint();
        chartContainer.add(checkBoxContainer);



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
    }

    private void addCheckBoxes(WeatherObservations observations) {
        WeatherObservation observation = observations.iterator().next();
        for(Field field: observation.getFields()) {
            JCheckBox fieldCheckBox = new JCheckBox(field.getLabel());
            fieldCheckBox.addActionListener(x -> addToGraph(field));
            checkBoxContainer.add(fieldCheckBox);
        }
    }

    private void addToGraph(Field field) {
        fieldsToGraph.add(field);
        log.debug("Field: " + field.getLabel() + " added to the list.");
    }

    /**
     * Takes the {@link WeatherObservation} object, and creates and populates a JTable with the most recent data.
     *
     * @param observations The iterable list of weather observations.
     * @return the populated table, which is added to the appropriate JPanel.
     */
    private JTable buildTable(WeatherObservations observations) {

        JTable table = new JTable(new WeatherObservationTableModel(observations));
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
        refresh.addActionListener(x -> show(station, new AsyncLoader(station).loadAsync()));
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
        chartPanel.setPopupMenu(null);
    }

    /**
     * Method to create the button to add or remove from the favourites list. Dynamically creates the label for the
     * button depending on the stations existence in the favourites list.
     *
     * @param station handle on the station object being operated on
     * @return new JButton to add or remove from favourites.
     */
    private JButton buildFavouritesButton(WeatherStation station) {
        JButton addRemoveFavourite = new JButton();
        if (station.isFavourite()) {
            addRemoveFavourite.setText(REMOVE_FROM_FAVOURITES);
        } else {
            addRemoveFavourite.setText(ADD_TO_FAVOURITES);
        }
        addRemoveFavourite.addActionListener(x -> toggleFavourites(station, addRemoveFavourite));
        return addRemoveFavourite;
    }

    /**
     * Method for toggling the favourites button between either Adding to Favourites, or Removing from Favourites.
     * Makes us of the {@link FavouritesUpdatedListener} to dynamically update the favourites list in the Main Window
     *
     * @param station handle on the station object being operated on.
     * @param button  handle on the favourite button
     */
    private void toggleFavourites(WeatherStation station, JButton button) {
        if (station.isFavourite()) {
            station.setFavourite(false);
            button.setText(ADD_TO_FAVOURITES);
        } else {
            button.setText(REMOVE_FROM_FAVOURITES);
            station.setFavourite(true);
        }
        favouritesUpdatedListener.update();
    }
}

