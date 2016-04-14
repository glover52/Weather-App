package org.chocvanilla.weatherapp;

import org.chocvanilla.weatherapp.chart.Chart;
import org.chocvanilla.weatherapp.data.*;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.*;

public class WeatherApp {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    final JFrame mainWindow = new JFrame("Weather App");
    
    private final WeatherStations weatherStations;
    private final Favourites favourites;
    private final JTextField searchBox = new JTextField();
    private final JList<WeatherStation> stationList = new JList<>();

    public WeatherApp(WeatherStations stations)  {
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.addWindowListener(new AppListener());
        weatherStations = stations;
        favourites = new Favourites(weatherStations);
        favourites.loadFromFile();
    }
    
    private void attachChart(JButton favouriteButton, WeatherStation station){
        FutureTask<XYDataset> task = new FutureTask<>(() -> Chart.createDataSet(station));
        executor.execute(task);
        favouriteButton.addActionListener(x -> openChart(station, task));
    }
    
    private void openChart(WeatherStation station, FutureTask<XYDataset> dataSupplier){
        try {
            new Chart(station, dataSupplier.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    public void run()  {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        
        container.add(buildFavouritesPanel());
        container.add(buildSearchPanel());

        mainWindow.setContentPane(container);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchBox, BorderLayout.NORTH);

        DefaultListModel<WeatherStation> model = new DefaultListModel<>();
        weatherStations.getStations().forEach(model::addElement);
        stationList.setModel(model);

        GuiHelpers.addChangeListener(searchBox, s -> {
            model.removeAllElements();
            weatherStations
                    .getStations()
                    .stream()
                    .filter(x -> x.toString().contains(searchBox.getText().toUpperCase()))
                    .forEach(model::addElement);
            }
        );

        JScrollPane scrollPane = new JScrollPane(stationList);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Weather Stations"));
        return searchPanel;
    }

    private JPanel buildFavouritesPanel() {
        JPanel favouritesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (WeatherStation station : favourites){
            JButton favouriteButton = new JButton(station.toString());
            favouritesPanel.add(favouriteButton);
            attachChart(favouriteButton, station);
        }
        favouritesPanel.setBorder(BorderFactory.createTitledBorder("Favourites"));
        favouritesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return favouritesPanel;
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //ignore
        }
        WeatherStations all = new WeatherStations();
        all.load();
        WeatherApp instance = new WeatherApp(all);
        instance.run();
    }

}
