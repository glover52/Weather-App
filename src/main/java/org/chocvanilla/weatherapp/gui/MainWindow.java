package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.*;

public class MainWindow {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final JFrame frame = new JFrame("Weather App");
    private final JFrame detailedFrame = new JFrame();
    private final WeatherStations stations;
    private final Favourites favourites;
    private final JTextField searchBox = new JTextField();
    private final JList<WeatherStation> stationList = new JList<>();

    public MainWindow(WeatherStations weatherStations, Favourites favouriteStations) {
        frame.setName("MainWindow");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowLocationManager(new Rectangle(800, 600), null));
        stations = weatherStations;
        favourites = favouriteStations;
    }

    public void run() {
        Container container = frame.getContentPane();
        container.add(buildFavouritesPanel(), BorderLayout.CENTER);
        container.add(buildSearchPanel(), BorderLayout.WEST);
        frame.pack();
        frame.setVisible(true);
        detailedFrame.addWindowListener(new WindowLocationManager(null, frame));
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBox, BorderLayout.NORTH);

        DefaultListModel<WeatherStation> model = new DefaultListModel<>();
        stations.getStations().forEach(model::addElement);
        stationList.setModel(model);

        GuiHelpers.addChangeListener(searchBox, s -> filterStations(model));
        stationList.addListSelectionListener(this::openChart);


        JScrollPane scrollPane = new JScrollPane(stationList);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Weather Stations"));
        return searchPanel;
    }

    private void filterStations(DefaultListModel<WeatherStation> model) {
        for (ListSelectionListener l : stationList.getListSelectionListeners()) {
            stationList.removeListSelectionListener(l);
        }
        model.removeAllElements();
        stations
                .getStations()
                .stream()
                .filter(x -> x.toString().contains(searchBox.getText().toUpperCase()))
                .forEach(model::addElement);
        stationList.addListSelectionListener(this::openChart);
    }

    private void openChart(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            WeatherStation station = stationList.getSelectedValue();
            if (station != null) {
                FutureTask<XYDataset> task = loadDataAsync(station);
                openChart(station, task);
                stationList.clearSelection();
            }
        }
    }

    private JPanel buildFavouritesPanel() {
        JPanel favouritesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (WeatherStation station : favourites) {
            JButton favouriteButton = new JButton(station.toString());
            favouritesPanel.add(favouriteButton);
            attachChart(favouriteButton, station);
        }
        favouritesPanel.setBorder(BorderFactory.createTitledBorder("Favourites"));
        return favouritesPanel;
    }

    private FutureTask<XYDataset> loadDataAsync(WeatherStation station) {
        FutureTask<XYDataset> task = new FutureTask<>(() -> ChartHelpers.createDataSet(station));
        executor.execute(task);
        return task;
    }

    private void attachChart(JButton favouriteButton, WeatherStation station) {
        FutureTask<XYDataset> task = loadDataAsync(station);
        favouriteButton.addActionListener(x -> openChart(station, task));
    }

    private void openChart(WeatherStation station, FutureTask<XYDataset> dataSupplier) {
        try {
            // Chart
            JPanel chartContainer = new JPanel();
            chartContainer.setLayout(new BoxLayout(chartContainer, BoxLayout.Y_AXIS));
            ChartPanel panel = ChartHelpers.createChart(station, dataSupplier.get());
            panel.setBorder(BorderFactory.createTitledBorder("Temperature History"));

            // Details
            // Get latest observations
            // FIXME: Observation loader make more sense to be static.
            ObservationLoader ol = new ObservationLoader();
            WeatherObservation ob = null;
            try {
                ob = ol.load(station).get(0);
            } catch (IOException e) {
                //TODO: handle this
            }
            // Chance of NPE
            JPanel detailedContainer = new JPanel();
            detailedContainer.add(buildDetails(ob));
            detailedContainer.setBorder(BorderFactory.createTitledBorder("Latest Observations"));

            chartContainer.add(panel);
            detailedFrame.setTitle(station.getName());
            detailedFrame.setContentPane(chartContainer);
            detailedFrame.add(detailedContainer);
            detailedFrame.setName("DetailWindow");
            detailedFrame.pack();
            detailedFrame.setVisible(true);
        } catch (InterruptedException | ExecutionException ignored) {
        }

    }
    private JPanel buildDetails (WeatherObservation observation){
        JPanel details = new JPanel();
        details.setLayout(new FlowLayout());

        final String DEG_C = "%.1f°C";
        final String KM_H = "%.1f km/h";
        details.add(fieldToLabel("Air Temp", String.format(DEG_C, observation.getAirTemperature()), details));
        details.add(fieldToLabel("Apparent Temp", String.format(DEG_C,observation.getApparentTemprature()), details));
        details.add(fieldToLabel("Gust", String.format(KM_H, observation.getGustKm()), details));
        details.add(fieldToLabel("Wind Speed", String.format(KM_H, observation.getWindSpdKm()), details));
        details.add(fieldToLabel("Wind Direction", observation.getWindDir(), details));
        details.add(fieldToLabel("Rain", String.format("%.1f mm",observation.getRain()), details));
        details.add(fieldToLabel("Dew point", String.format(DEG_C, observation.getDewPt()), details));

        return details;
    }

    private JPanel fieldToLabel(String label, Object data, JPanel parent) {
        JPanel entry = new JPanel();
        entry.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Put label on top
        c.gridx = 0;
        c.gridy = 0;
        entry.add(new JLabel("<html><b>"+label+"</b></html>"), c);
        // Put data on bottom
        c.gridy = 1;
        entry.add(new JLabel(String.valueOf(data)), c);
        // spacers
        parent.add(new JSeparator(SwingConstants.VERTICAL));

        return entry;
    }

}
