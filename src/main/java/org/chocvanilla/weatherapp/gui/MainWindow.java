package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.chart.ChartHelpers;
import org.chocvanilla.weatherapp.data.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

public class MainWindow {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final JFrame frame = new JFrame("Weather App");
    private final DetailWindow detailWindow = new DetailWindow(
            new WindowLocationManager(null, frame),
            this::updateFavouritesButtons);
    private final WeatherStations stations;
    private final Favourites favourites;
    private final JTextField searchBox = new JTextField();
    private final JList<WeatherStation> stationList = new JList<>();
    private JPanel favouritesPanel;

    public MainWindow(WeatherStations weatherStations, Favourites favouriteStations) {
        stations = weatherStations;
        favourites = favouriteStations;
        frame.setName("MainWindow");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowLocationManager(new Rectangle(800, 600), null));
        frame.addWindowListener(new FavouritesManager(favourites));
    }

    public void run() {
        Container container = frame.getContentPane();
        container.add(buildFavouritesPanel(), BorderLayout.CENTER);
        container.add(buildSearchPanel(), BorderLayout.WEST);
        frame.pack();
        frame.setVisible(true);
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
        favouritesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updateFavouritesButtons();
        favouritesPanel.setBorder(BorderFactory.createTitledBorder("Favourites"));
        return favouritesPanel;
    }

    private void updateFavouritesButtons() {
        favouritesPanel.removeAll();
        if (favourites.isEmpty()) {
            favouritesPanel.add(new JLabel("You have no favorites! Open a station and click the favorites button to see it here."));
        }
        for (WeatherStation station : favourites) {
            JButton favouriteButton = new JButton(station.toString());
            favouritesPanel.add(favouriteButton);
            attachChart(favouriteButton, station);
        }
        favouritesPanel.revalidate();
        favouritesPanel.repaint();
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
        detailWindow.display(station, dataSupplier, favourites);
    }

}


interface FavouritesUpdatedListener {
    public void update();
}
