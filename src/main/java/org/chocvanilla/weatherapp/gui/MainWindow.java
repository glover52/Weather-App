package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.forecast.ForecastProvider;
import org.chocvanilla.weatherapp.data.observations.*;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.chocvanilla.weatherapp.io.AsyncLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class MainWindow {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final String NO_FAVOURITES =
            "<HTML>You have no favorites! <br> Open a station and click the favorites button to see it here.</HTML>";
    private static final String ADD_TO_FAVOURITES = "☆ Favourite";
    private static final String REMOVE_FROM_FAVOURITES = "★ Unfavourite";
    private final JFrame frame = new JFrame("Weather App");
    private final WeatherStations stations;
    private final ObservationsProvider observationsProvider;
    private final ForecastProvider forecastProvider;
    private final DetailWindow detailWindow;
    private final JTextField searchBox = new JTextField();
    private final JList<WeatherStation> stationList = new JList<>();
    private JPanel favouritesPanel;
    private JPanel townPanel;


    public MainWindow(WeatherStations weatherStations,
                      ObservationsProvider provider,
                      ForecastProvider forecastProvider,
                      DetailWindow details) {
        stations = weatherStations;
        this.observationsProvider = provider;
        this.forecastProvider = forecastProvider;
        frame.setName("MainWindow");
        log.debug("{} created with {} weather stations", frame.getName(), weatherStations.size());
        detailWindow = details;
        detailWindow.setFavouritesListener(this::updateFavouritesButtons);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(930, 600));
        buildContainerLayout();
        frame.pack();
        searchBox.requestFocusInWindow();
    }

    public void addListener(WindowListener l) {
        frame.addWindowListener(l);
    }

    public void show() {
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

        SwingUtilities.invokeLater(() -> {
            FutureTask<WeatherObservations> source = 
                    new AsyncLoader(station).loadAsync(observationsProvider::loadObservations);
            new AsyncLoader(station).loadAsync(forecastProvider::loadForecast); // preload forecast
            addCurrentWeatherToTownPanel(source);
        });


        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel topPanel = new JPanel((new BorderLayout()));

        topPanel.add(new JLabel(station.getName()), BorderLayout.NORTH);

        JButton showForecast = new JButton("Forecast");
        JButton showBOMObservation = new JButton("BOM Observations");
        JButton showForcastIOObservation = new JButton("Forecast.io Observations");
        JButton toggleFavourite = buildFavouritesButton(station);


        showForecast.addActionListener(x -> openForecast(station));
        showBOMObservation.addActionListener(x -> openBOMObservations(station));
        showForcastIOObservation.addActionListener(x -> openForecastIOObservation(station));

        buttonPanel.add(showForecast);
        buttonPanel.add(showBOMObservation);
        buttonPanel.add(showForcastIOObservation);
        topPanel.add(toggleFavourite);

        townPanel.add(buttonPanel, BorderLayout.SOUTH);
        townPanel.add(topPanel, BorderLayout.NORTH);

        townPanel.revalidate();
        townPanel.repaint();
    }

    private void openForecastIOObservation(WeatherStation station) {

    }

    private void addCurrentWeatherToTownPanel(FutureTask<WeatherObservations> source) {
        try {
            WeatherObservations weatherObservations = source.get();
            Iterator<WeatherObservation> it = weatherObservations.iterator();
            if (it.hasNext()) {
                JPanel observationPanel = GuiHelpers.buildDetails(it.next());
                observationPanel.setLayout(new BoxLayout(observationPanel, BoxLayout.Y_AXIS));
                townPanel.add(observationPanel, 0);
                // only add first observation to town panel
            } else {
                log.error("Unable to display current weather");             
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void openBOMObservations(WeatherStation station) {
        if (station != null) {
            openChart(station, observationsProvider);
        }
    }

    private void openForecast(WeatherStation station) {
        openChart(station, forecastProvider::loadForecast);
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

    private JPanel buildFavouritesPanel() {
        favouritesPanel = new JPanel();
        favouritesPanel.setLayout(new BoxLayout(favouritesPanel, BoxLayout.Y_AXIS));
        updateFavouritesButtons();
        favouritesPanel.setBorder(BorderFactory.createTitledBorder("Favourites"));
        return favouritesPanel;
    }

    private void updateFavouritesButtons() {
        favouritesPanel.removeAll();
        Iterable<WeatherStation> favourites = () -> stations.getFavourites().iterator();
        boolean hasFavourites = false;
        for (WeatherStation station : favourites) {
            hasFavourites = true;
            JButton favouriteButton = new JButton(station.toString());
            Dimension d = favouriteButton.getPreferredSize();
            d.width = 500;
            favouriteButton.setMaximumSize(d);
            favouritesPanel.add(favouriteButton);//, constraints);
            favouriteButton.addActionListener(x -> updateTownPanel(station));
        }
        if (!hasFavourites) {
            favouritesPanel.add(new JLabel(NO_FAVOURITES));
        }
        favouritesPanel.revalidate();
        favouritesPanel.repaint();
    }

    private void openChart(WeatherStation station, ObservationsProvider provider) {
        detailWindow.show(station, provider);
    }

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

    private void toggleFavourites(WeatherStation station, JButton button) {
        if (station.isFavourite()) {
            station.setFavourite(false);
            button.setText(ADD_TO_FAVOURITES);
        } else {
            button.setText(REMOVE_FROM_FAVOURITES);
            station.setFavourite(true);
        }
        updateFavouritesButtons();
    }

    public Component getComponent() {
        return frame;
    }
}


