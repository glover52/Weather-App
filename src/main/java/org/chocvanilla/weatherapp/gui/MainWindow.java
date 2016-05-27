package org.chocvanilla.weatherapp.gui;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.observations.WeatherObservations;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class MainWindow {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String NO_FAVOURITES =
            "You have no favorites! Open a station and click the favorites button to see it here.";
    private final JFrame frame = new JFrame("Weather App");
    private final WeatherStations stations;
    private final DetailWindow detailWindow;
    private final JTextField searchBox = new JTextField();
    private final JList<WeatherStation> stationList = new JList<>();
    private JPanel favouritesPanel;
    private JPanel townPanel;

    private Dimension d = new Dimension(800, 600);

    public MainWindow(Gson gson, WeatherStations weatherStations) {
        stations = weatherStations;
        frame.setName("MainWindow");
        log.debug("{} created with {} weather stations", frame.getName(), weatherStations.size());
        detailWindow = new DetailWindow(
                new WindowLocationManager(gson, frame),
                this::updateFavouritesButtons);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowLocationManager(gson, new Rectangle(800, 600)));
        frame.addWindowListener(new FavouritesManager(weatherStations));
        frame.setMinimumSize(d);
        buildContainerLayout();
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    private void buildContainerLayout() {
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weighty = 1;

        container.add(buildFavouritesPanel(), constraints);
        constraints.gridx = 1;

        container.add(buildSearchPanel(), constraints);

        constraints.weightx = 1;
        constraints.gridx = 2;
        container.add(buildTownPanel(), constraints);
    }

    private JPanel buildTownPanel() {
        townPanel = new JPanel(new BorderLayout());
        townPanel.add(new JLabel("No Town Selected"), BorderLayout.NORTH);
        return townPanel;
    }
    
    

    private void updateTownPanel(WeatherStation station) {
        townPanel.removeAll();
        
        SwingUtilities.invokeLater(() -> addCurrentWeatherToTownPanel(station.loadAsync()));
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton showForecast = new JButton("Forecast");
        JButton showObservation = new JButton("Observations");

        showForecast.addActionListener(x -> openForecast(station));
        showObservation.addActionListener(x -> openObservations(station));

        buttonPanel.add(showForecast);
        buttonPanel.add(showObservation);

        
        townPanel.add(new JLabel(station.getName()), BorderLayout.NORTH);
        townPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        townPanel.revalidate();
        townPanel.repaint();
    }

    private void addCurrentWeatherToTownPanel(FutureTask<WeatherObservations> source) {
        try {
            JPanel observationPanel = GuiHelpers.buildDetails(source.get().iterator().next());
            observationPanel.setLayout(new BoxLayout(observationPanel, BoxLayout.Y_AXIS));
            townPanel.add(observationPanel, 0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void openObservations(WeatherStation station) {
        if (station != null) {
            openChart(station, station.loadAsync());
        }
    }

    private void openForecast(WeatherStation station) {
        //open forecast using town object
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBox, BorderLayout.NORTH);

        DefaultListModel<WeatherStation> model = new DefaultListModel<>();
        stations.forEach(model::addElement);
        stationList.setModel(model);

        GuiHelpers.addChangeListener(searchBox, s -> filterStations(model));
        stationList.addListSelectionListener(this::displayStation);


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
        stations.stream()
                .filter(x -> x.toString().contains(searchBox.getText().toUpperCase()))
                .forEach(model::addElement);
        //stationList.addListSelectionListener(this::openChart);
        stationList.addListSelectionListener(this::displayStation);
    }

    private void displayStation(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            WeatherStation station = stationList.getSelectedValue();
            updateTownPanel(station);
        }
    }

    private void openChart(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            WeatherStation station = stationList.getSelectedValue();
            if (station != null) {
                openChart(station, station.loadAsync());
                stationList.clearSelection();
            }
        }
    }

    private JPanel buildFavouritesPanel() {
        favouritesPanel = new JPanel();
        favouritesPanel.setLayout(new GridBagLayout());
        updateFavouritesButtons();
        favouritesPanel.setBorder(BorderFactory.createTitledBorder("Favourites"));
        return favouritesPanel;
    }

    private void updateFavouritesButtons() {
        favouritesPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        Iterable<WeatherStation> favourites = () -> stations.getFavourites().iterator();
        boolean hasFavourites = false;
        for (WeatherStation station : favourites) {
            hasFavourites = true;
            JButton favouriteButton = new JButton(station.toString());
            favouritesPanel.add(favouriteButton, constraints);
            attachChart(favouriteButton, station);
        }
        if (!hasFavourites) {
            favouritesPanel.add(new JLabel(NO_FAVOURITES));
        }
        favouritesPanel.revalidate();
        favouritesPanel.repaint();
    }

    private void attachChart(JButton favouriteButton, WeatherStation station) {
        favouriteButton.addActionListener(x -> openChart(station, station.loadAsync()));
    }

    private void openChart(WeatherStation station, FutureTask<WeatherObservations> dataSupplier) {
        detailWindow.show(station, dataSupplier);
    }   
    
}


