package org.chocvanilla.weatherapp.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Favourites {

	private ArrayList<WeatherStation> list = new ArrayList<WeatherStation>();

	public Favourites() {

	}

	public boolean saveToFile(String n) {
		File f = new File(n);
		
		try {
			PrintWriter pw = new PrintWriter(f);
			f.createNewFile();
			
			for (WeatherStation ws : list) {
				String id = Integer.toString(ws.getWmoNumber());
				
				pw.println(id);
			}
			
			pw.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + n);
			return false;
		}
		catch (IOException e) { return false; }

		return true;
	}

	public boolean loadFromFile(String n) {
		try {
			File f = new File(n);
			BufferedReader br = new BufferedReader(new FileReader(f));

			@SuppressWarnings("unused")
			String line;
			while ((line = br.readLine()) != null) {
				// Implement me
			}

			br.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + n);
			return false;
		}
		catch (IOException e) { return false; }

		return true;
	}

	public WeatherStation getFavourite(int i) {
		return list.get(i);
	}

	public void addToFavourites(WeatherStation ws) {
		list.add(ws);
	}

	public void removeFromFavourites(WeatherStation ws) {
		list.remove(ws);
	}
	
	private int getSize() {
		return list.size();
	}
}
