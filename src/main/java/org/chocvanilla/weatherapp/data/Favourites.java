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
     * @throws IOException if the Favourites could not be loaded from disk
     */
    public static Favourites loadFromFile(WeatherStations allStations) throws IOException {
        Favourites favourites = new Favourites(allStations);

        try (BufferedReader reader = Files.newBufferedReader(FAVOURITES_PATH)) {
            // parse each line as an integer and add it to the list of station numbers
            reader.lines().map(Integer::valueOf).forEach(favourites.wmoNumbers::add);
        } catch (NoSuchFileException ignored) {
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
    public boolean contains(Object o) {
        return wmoNumbers.contains(o);
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
