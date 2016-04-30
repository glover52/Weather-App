package org.chocvanilla.weatherapp.gui;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.concurrent.FutureTask;


public class MainWindow {

    private static final String NO_FAVOURITES =
            "You have no favorites! Open a station and click the favorites button to see it here.";
    private final JFrame frame = new JFrame("Weather App");
    private final WeatherStations stations;
    private final DetailWindow detailWindow;
    private final JTextField searchBox = new JTextField();
    private final JList<BomWeatherStation> stationList = new JList<>();
    private JPanel favouritesPanel;

    public MainWindow(Gson gson, WeatherStations weatherStations) {
        stations = weatherStations;
        frame.setName("MainWindow");
        detailWindow = new DetailWindow(
                new WindowLocationManager(gson, frame),
                this::updateFavouritesButtons);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowLocationManager(gson, new Rectangle(800, 600)));
        frame.addWindowListener(new FavouritesManager(weatherStations));
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

        DefaultListModel<BomWeatherStation> model = new DefaultListModel<>();
        stations.getStations().forEach(model::addElement);
        stationList.setModel(model);

        GuiHelpers.addChangeListener(searchBox, s -> filterStations(model));
        stationList.addListSelectionListener(this::openChart);


        JScrollPane scrollPane = new JScrollPane(stationList);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Weather Stations"));
        return searchPanel;
    }

    private void filterStations(DefaultListModel<BomWeatherStation> model) {
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
            BomWeatherStation station = stationList.getSelectedValue();
            if (station != null) {
                openChart(station, station.loadAsync());
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
        Iterable<BomWeatherStation> favourites = () -> stations.getFavourites().iterator();
        boolean hasFavourites = false;
        for (BomWeatherStation station : favourites) {
            hasFavourites = true;
            JButton favouriteButton = new JButton(station.toString());
            favouritesPanel.add(favouriteButton);
            attachChart(favouriteButton, station);
        }
        if (!hasFavourites) {
            favouritesPanel.add(new JLabel(NO_FAVOURITES));
        }
        favouritesPanel.revalidate();
        favouritesPanel.repaint();
    }

    private void attachChart(JButton favouriteButton, BomWeatherStation station) {
        favouriteButton.addActionListener(x -> openChart(station, station.loadAsync()));
    }

    private void openChart(BomWeatherStation station, FutureTask<WeatherObservations> dataSupplier) {
        detailWindow.display(station, dataSupplier);
    }

}


