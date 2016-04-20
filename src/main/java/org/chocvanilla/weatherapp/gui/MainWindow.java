package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
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
                FutureTask<List<WeatherObservation>> task = loadDataAsync(station);
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

    private FutureTask<List<WeatherObservation>> loadDataAsync(WeatherStation station) {
        FutureTask<List<WeatherObservation>> task = new FutureTask<>(() -> ChartHelpers.loadObservations(station));
        executor.execute(task);
        return task;
    }

    private void attachChart(JButton favouriteButton, WeatherStation station) {
        FutureTask<List<WeatherObservation>> task = loadDataAsync(station);
        favouriteButton.addActionListener(x -> openChart(station, task));
    }

    private void openChart(WeatherStation station, FutureTask<List<WeatherObservation>> dataSupplier) {
        try {
            // Chart
            JPanel chartContainer = new JPanel();
            chartContainer.setLayout(new BoxLayout(chartContainer, BoxLayout.Y_AXIS));
            List<WeatherObservation> observations = dataSupplier.get();
            ChartPanel panel = ChartHelpers.createChart(station, observations);
            panel.setBorder(BorderFactory.createTitledBorder("Temperature History"));

            // Details
            WeatherObservation ob = observations.get(0);
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

        final String DEG_C = "%.1f °C";
        final String KM_H = "%.1f km/h";
        details.add(fieldToLabel("Air Temp", String.format(DEG_C, observation.getAirTemperature()), details));
        details.add(fieldToLabel("Apparent Temp", String.format(DEG_C,observation.getApparentTemperature()), details));
        details.add(fieldToLabel("Gust", String.format(KM_H, observation.getGustKm()), details));
        details.add(fieldToLabel("Wind Speed", String.format(KM_H, observation.getWindSpdKm()), details));
        details.add(fieldToLabel("Wind Direction", observation.getWindDir(), details));
        details.add(fieldToLabel("Rain", String.format("%.1f mm",observation.getRain()), details));
        details.add(fieldToLabel("Dew point", String.format(DEG_C, observation.getDewPt()), details));

        return details;
    }

    private JPanel fieldToLabel(String label, Object data, JPanel parent) {
        JPanel entry = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Put label on top
        c.gridx = 0;
        c.gridy = 0;
        entry.add(new JLabel("<html><b>"+label+"</b></html>"), c);
        // Put data on bottom
        c.gridy = 1;
        entry.add(new JLabel(data.toString()), c);
        // spacers
        parent.add(new JSeparator(SwingConstants.VERTICAL));

        return entry;
    }

}
