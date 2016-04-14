package org.chocvanilla.weatherapp.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Favourites extends AbstractCollection<WeatherStation> {
    private static final Path FAVOURITES_PATH = Paths.get("favourites.dat");
    private final WeatherStations all;
    private Set<Integer> wmoNumbers = new HashSet<>();

    private Favourites(WeatherStations allStations) {
        all = allStations;
    }

    public static Favourites loadFromFile(WeatherStations allStations) throws IOException {
        Favourites favourites = new Favourites(allStations);

        try (BufferedReader reader = Files.newBufferedReader(FAVOURITES_PATH)) {
            reader.lines().map(Integer::valueOf).forEach(favourites.wmoNumbers::add);

        } catch (NoSuchFileException e) {
        }
        return favourites;
    }

    public boolean saveToFile() {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH))) {
            for (int wmoNumber : wmoNumbers) {
                String id = Integer.toString(wmoNumber);
                pw.println(id);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
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
        return wmoNumbers.stream().map(all::getByWmoNumber).iterator();
    }

    @Override
    public int size() {
        return wmoNumbers.size();
    }
}
