package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new AppListener());
        stations = weatherStations;
        favourites = favouriteStations;
    }

    public void run() {
        Container container = frame.getContentPane();
        container.add(buildFavouritesPanel(), BorderLayout.CENTER);
        container.add(buildSearchPanel(), BorderLayout.WEST);
        frame.pack();
        frame.setVisible(true);
        detailedFrame.setLocationRelativeTo(frame);
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
            JPanel detailedContainer = new JPanel();
            detailedContainer.setLayout(new BoxLayout(detailedContainer, BoxLayout.Y_AXIS));

            ChartPanel panel = ChartHelpers.createChart(station, dataSupplier.get());

            detailedContainer.add(panel);
            detailedFrame.setTitle(station.getName());
            detailedFrame.setContentPane(detailedContainer);
            detailedFrame.pack();
            detailedFrame.setVisible(true);
        } catch (InterruptedException | ExecutionException ignored) {
        }
    }

}
