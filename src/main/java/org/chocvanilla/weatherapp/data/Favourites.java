package org.chocvanilla.weatherapp.data;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Favourites implements Iterable<WeatherStation>{
	private static final Path FAVOURITES_PATH = Paths.get("favourites.dat");
	private final WeatherStations all;
	private ArrayList<Integer> wmoNumbers = new ArrayList<>();

	public Favourites(WeatherStations allStations){
		all = allStations;
	}

	public boolean saveToFile() {
		try (BufferedWriter writer = Files.newBufferedWriter(FAVOURITES_PATH)) {
			PrintWriter pw = new PrintWriter(writer);
			for (int wmoNumber : wmoNumbers) {
				String id = Integer.toString(wmoNumber);
				pw.println(id);
			}
		} catch (IOException e) { return false; }
		return true;
	}

	public boolean loadFromFile() {
		try (BufferedReader reader = Files.newBufferedReader(FAVOURITES_PATH)){
			String line;
			while ((line = reader.readLine()) != null) {
				int id = Integer.parseInt(line);
				wmoNumbers.add(id);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + FAVOURITES_PATH);
			return false;
		}
		catch (IOException e) { return false; }

		return true;
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
