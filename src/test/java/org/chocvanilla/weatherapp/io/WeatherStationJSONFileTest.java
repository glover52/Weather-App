package org.chocvanilla.weatherapp.io;

import com.google.gson.Gson;
import org.chocvanilla.weatherapp.data.stations.WeatherStation;
import org.chocvanilla.weatherapp.data.stations.WeatherStations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeatherStationJSONFileTest {
    private final Gson gson = new Gson();
    private WeatherStationsJSONFile source;
    private static final String TARGET  = ".preferences";
    private static final Path FAVOURITES_PATH = Paths.get(TARGET, "favourites.dat");

    @Before
    public void setUp() {
        source = new WeatherStationsJSONFile(gson);
    }

    @Test
    public void testLoad() throws IOException{
        List<WeatherStation> ws = source.load();
        assertThat(ws, is(not(empty())));
    }

    @Test
    public void weatherStationsJSONFileIsPresent() throws IOException {
        WeatherStations stations = WeatherStations.loadFrom(source);
        assertThat(stations, is(not(empty())));
    }

    @Test
    public void favoriteStationsAreSaved() throws IOException{
        WeatherStations ws = WeatherStations.loadFrom(source);
        Set<String> favs = new HashSet<>();

        int i = 0;
        for ( WeatherStation s : ws) {
            if (i < 5) s.setFavourite(true); i++;
        }

        ws.getFavourites()
                .filter(WeatherStation::isFavourite)
                .forEach( x-> {
                    assertTrue(x.isFavourite());
                    favs.add(x.getUniqueID());
                });

        ws.save();

        try (Stream<String> stream = Files.lines(FAVOURITES_PATH)) {
            stream.forEach(x -> assertTrue(favs.contains(x)));
        }
    }
}
