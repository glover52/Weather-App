package org.chocvanilla.weatherapp.data;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Favourites implements Iterable<WeatherStation>{
	private static final Path FAVOURITES_PATH = Paths.get("favourites.dat");
	private final WeatherStations all;
	private ArrayList<Integer> wmoNumbers = new ArrayList<>();

	private Favourites(WeatherStations allStations){
		all = allStations;
	}

	public boolean saveToFile() {
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(FAVOURITES_PATH))) {
			for (int wmoNumber : wmoNumbers) {
				String id = Integer.toString(wmoNumber);
				pw.println(id);
			}
		} catch (IOException e) { return false; }
		return true;
	}

	public static Favourites loadFromFile(WeatherStations allStations) throws IOException {
		Favourites favourites = new Favourites(allStations);

		try (BufferedReader reader = Files.newBufferedReader(FAVOURITES_PATH)){
			reader.lines().map(Integer::valueOf).forEach(favourites.wmoNumbers::add);

		}
		catch(NoSuchFileException e) { }
		return favourites;
	}

	public WeatherStation getFavourite(int i) {
		return all.getByWmoNumber(wmoNumbers.get(i));
	}

	public void addToFavourites(WeatherStation ws) {
		wmoNumbers.add(ws.getWmoNumber());
	}

	public void removeFromFavourites(WeatherStation ws) {
		wmoNumbers.remove(ws.getWmoNumber());
	}
	
	private int getSize() {
		return wmoNumbers.size();
	}

	@Override
	public Iterator<WeatherStation> iterator() {
		return wmoNumbers.stream().map(all::getByWmoNumber).iterator();
	}
}
