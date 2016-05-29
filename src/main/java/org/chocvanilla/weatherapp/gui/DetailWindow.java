package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.observations.*;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowListener;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DetailWindow {
    private static final String REFRESH = "↻ Refresh";
    private static final String ADD_TO_FAVOURITES = "☆ Favourite";
    private static final String REMOVE_FROM_FAVOURITES = "★ Unfavourite";

    private static final String OPEN_SOURCE_COMPONENTS =
            "<HTML> Graph made using: JFreeChart" +
                    "<br>" +
                    "Observations supplied by: Bureau of Meteorology and Forecast.io" +
                    "<br>" +
                    "Forecast supplied by: Forecast.io </HTML>";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final JFrame frame = new JFrame();
    private final JPanel latestObsContainer = new JPanel();
    private final JPanel chartContainer = new JPanel();
    private final JPanel checkBoxContainer = new JPanel();
    private final JPanel tableContainer = new JPanel();
    private final JPanel buttonContainer = new JPanel();
    private final JTabbedPane historyContainer = new JTabbedPane();
    private final JLabel refreshStatusLabel = new JLabel();

    private ChartPanel chartPanel = null;
    private ArrayList<String> fieldsToGraph = new ArrayList<>();
    private FavouritesUpdatedListener favouritesUpdatedListener;

    private Map<String, Integer> dataSetIndex = new HashMap<>();

    /**
     * Create a new window, in which a chart with the most recent data is displayed, both as a chart, and in a
     * table. View is modelled in tabs. Data can be toggled on and off the chart.
     */
    public DetailWindow() {
        frame.setName("DetailWindow");
        frame.setMinimumSize(new Dimension(1150, 665));

        JPanel container = new JPanel();
        frame.setContentPane(container);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // set layout managers
        buttonContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        latestObsContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));
        checkBoxContainer.setLayout(new BoxLayout(checkBoxContainer, BoxLayout.Y_AXIS));
        chartContainer.setLayout(new BorderLayout());

        // add panels to frame
        container.add(buttonContainer);
        container.add(latestObsContainer);
        container.add(buildHistoryContainer());

        // Open source recognitions
        chartContainer.add(new JLabel(OPEN_SOURCE_COMPONENTS), BorderLayout.SOUTH);
    }

    /**
     * Method to create tabs to contain both teh graph and table data.
     * @return The tabbed JPane.
     */
    private JTabbedPane buildHistoryContainer() {
        tableContainer.setLayout(new GridBagLayout());
        historyContainer.setBorder(BorderFactory.createTitledBorder("Weather Observations"));
        historyContainer.addTab("Chart", chartContainer);
        historyContainer.addTab("Table", tableContainer);
        return historyContainer;
    }

    public void addListener(WindowListener l) {
        frame.addWindowListener(l);
    }

    public void setFavouritesListener(FavouritesUpdatedListener l) {
        favouritesUpdatedListener = l;
    }

    /**
     * The method that deals with the Java Swing context. All containers are cleared and populated with fresh data.
     *
     * @param station  The {@link WeatherStation} for which data is displayed
     * @param provider An asynchronous way of retrieving the {@link WeatherObservation} data
     */
    public void show(WeatherStation station, ObservationsProvider provider) {
        fieldsToGraph.clear();

        WeatherObservations observations = provider.loadObservations(station);
        if (observations.isEmpty()) {
            MessageBox.show("Unfortunately, observations are not available for this station.", "Error");
        }
        populateDataSetIndex(observations);


        latestObsContainer.removeAll();
        latestObsContainer.add(GuiHelpers.buildDetails(observations.iterator().next()));
        // Add to favorites
        buttonContainer.removeAll();
        buttonContainer.add(buildFavouritesButton(station));
        buttonContainer.add(buildRefreshButton(station, provider));
        buttonContainer.add(refreshStatusLabel);

        // Need to revalidate to avoid artifacts from previous button
        buttonContainer.revalidate();
        buttonContainer.repaint();

        // Chart
        JFreeChart chart = ChartHelpers.createChart(station, observations);
        updateChart(chart);
        // Checkboxes for chart
        addCheckBoxes(observations, chart);
        chartContainer.add(checkBoxContainer, BorderLayout.EAST);

        // Table
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0;
        tableContainer.removeAll();
        tableContainer.add(new JScrollPane(buildTable(observations)), c);

        frame.setTitle(station.getName());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Populates a HashMap of integers, where the key is the name of the field. Used for adding and removing items from
     * the graph.
     * @param observations gives list of fields to graph.
     */
    private void populateDataSetIndex(WeatherObservations observations) {
        WeatherObservation observation = observations.iterator().next();
        int x = 0;
        for (Field field : observation.getFields()) {
            if (field.isGraphable()) {
                dataSetIndex.put(field.getLabel(), x);
                x++;
            }
        }
        log.debug(dataSetIndex.toString());
    }

    /**
     * Shows time since the last JSON was downloaded.
     * @param millis the number of milliseconds elapsed
     */
    public void updateTimeSinceLastRefresh(long millis) {
        refreshStatusLabel.setText(String.format("Last refresh: %d seconds ago.",
                TimeUnit.MILLISECONDS.toSeconds(millis)));

        Timer timer = new Timer(1000, x -> refreshStatusLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Adds checkbox's for the fields dynamically to the graph.
     * @param observations the observations, required to get list of fields that can be graphed.
     * @param chart the chart object being passed to the toggle function.
     */
    private void addCheckBoxes(WeatherObservations observations, JFreeChart chart) {
        WeatherObservation observation = observations.iterator().next();
        checkBoxContainer.removeAll();
        if (fieldsToGraph.size() == 0) {
            fieldsToGraph.add("Air Temp");
        }

        // If field is a quantifiable data item, allows user to graph it.
        for (Field field : observation.getFields()) {
            if (field.isGraphable()) {
                JCheckBox fieldCheckBox = new JCheckBox(field.getLabel(), fieldsToGraph.contains(field.getLabel()));
                fieldCheckBox.addActionListener(x ->
                        toggleGraph(fieldCheckBox, field, chart, observations));
                checkBoxContainer.add(fieldCheckBox);
            }
        }
    }

    /**
     * Decides whether the action adds or removes the field, depending on its current state. Will not allow user to
     * remove all items from the graph.
     * @param box The checkbox being checked. Required to stop user from removing ALL values from graph
     * @param field the field being toggled
     * @param chart the graph the field is being added or removed to/from
     * @param observations list of observations being added/removed.
     */
    private void toggleGraph(JCheckBox box, Field field, JFreeChart chart, WeatherObservations observations) {
        if (fieldsToGraph.contains(field.getLabel())) {
            if (fieldsToGraph.size() > 1) {
                fieldsToGraph.remove(field.getLabel());
                removeFieldFromGraph(field, chart);
                log.debug("Field: " + field.getLabel() + " removed from list.");
            } else {
                box.setSelected(true);
            }
        } else {
            fieldsToGraph.add(field.getLabel());
            addFieldToGraph(field, chart, observations);
            log.debug("Field: " + field.getLabel() + " added to list.");
        }
    }

    /**
     * Removes field in question from the graph.
     * @param field the field to be removed
     * @param chart the graph which the field is being removed from
     */
    private void removeFieldFromGraph(Field field, JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(dataSetIndex.get(field.getLabel()), null);
        // do not set the renderer to null, otherwise grid lines are lost
    }

    /**
     * Adds the field in question to the graph, and adds the plot line renderer.
     * @param field field to be added
     * @param chart chart in which the field is being added
     * @param observations context of Weather Observations being added to the graph.
     */
    private void addFieldToGraph(Field field, JFreeChart chart, WeatherObservations observations) {
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(dataSetIndex.get(field.getLabel()), ChartHelpers.createDataSet(field, observations));
        plot.setRenderer(dataSetIndex.get(field.getLabel()), new StandardXYItemRenderer());
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
    private JButton buildRefreshButton(WeatherStation station, ObservationsProvider provider) {
        JButton refresh = new JButton(REFRESH);
        refresh.addActionListener(x -> {
            show(station, provider);
            updateTimeSinceLastRefresh(0);
        });
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
            chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
            chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
            chartContainer.add(chartPanel, BorderLayout.CENTER);
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


