package org.chocvanilla.weatherapp.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * A {@link Collection<WeatherStation>} containing the favourite stations of the current user.
 */
public class Favourites extends AbstractCollection<WeatherStation> {
    private static final Path FAVOURITES_PATH = Paths.get(".favourites.dat");
    private final WeatherStations all;
    private final Set<Integer> wmoNumbers = new HashSet<>();
    
    private Favourites(WeatherStations allStations) {
        all = allStations;
    }


    /**
     * Load the current user's favourites from disk.
     * @param allStations all available weather stations
     * @return the loaded Favourites
     */
    public static Favourites loadFromFile(WeatherStations allStations) {
        Favourites favourites = new Favourites(allStations);

        try (BufferedReader reader = Files.newBufferedReader(FAVOURITES_PATH)) {
            // parse each line as an integer and add it to the list of station numbers
            reader.lines().map(Integer::valueOf)
                          .filter(wmoNumber -> allStations.getStations().stream()
                                  .anyMatch(station -> wmoNumber == station.getWmoNumber()))
                          .forEach(favourites.wmoNumbers::add);
        } catch (IOException ignored) {
            // no favourites yet
        }
        return favourites;
    }

    /**
     * Saves the current user's favourites to disk.
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH))) {
            // print each station number on a new line
            wmoNumbers.forEach(pw::println);
        }
    }
    
    @Override
    public boolean add(WeatherStation ws) {
        return wmoNumbers.add(ws.getWmoNumber());
    }

    @Override
    public boolean remove(Object o) {
        return remove((WeatherStation)o);
    }

    public boolean remove(WeatherStation ws) {return  wmoNumbers.remove(ws.getWmoNumber());}

    @Override
    public boolean contains(Object o) {
        return contains((WeatherStation)o);
    }


    public boolean contains(WeatherStation station) {
        return wmoNumbers.contains(station.getWmoNumber());
    }


    @Override
    public Iterator<WeatherStation> iterator() {
        // for each wmoNumber, obtain the corresponding WeatherStation
        return wmoNumbers.stream().map(all::getByWmoNumber).iterator();
    }

    @Override
    public int size() {
        return wmoNumbers.size();
    }
}
