#HOWTO

##Installation
To install and compile this software see **Building** for details.

Release versions of weather app are distributed in a zip file named weather-app-\<version number>.zip.
To use this only as an application, the user only needs to run the supplied "Weather-Application" JAR file.


```bash
$ mkdir ~/weather-app
$ unzip ~/Downloads/weather-app-0.1.zip -d ~/weather-app/
$ cd weather-app
$ java -jar weather-app-0.1.jar
```


##Usage

Please consult the [User Manual](User Manual.pdf) for a detailed explanation of the program.

###Updating stations:
Weather station information is stored in [.weather_stations.json](../.weather_stations.json) as an array of weather station objects.

If you wish to edit the weather stations known by the program, simply edit the [.weather_stations.json](../.weather_stations.json) file. This file should be located in the same directory as the jar file, or the root directory of the project.

####Adding a weather station
Weather stations objects in [.weather_stations.json](../.weather_stations.json) adhere to the following format:
```json
  {
    "stationID": 40764,
    "wmoNumber": 94580,
    "name": "GOLD COAST SEAWAY",
    "state": "QLD",
    "latitude": -27.939,
    "longitude": 153.4283
  }
```

To add a weather station, insert the appropriate object into file:

```json
[
  {
    "stationID": 40764,
    "wmoNumber": 94580,
    "name": "GOLD COAST SEAWAY",
    "state": "QLD",
    "latitude": -27.939,
    "longitude": 153.4283
  },
  {
    "stationID": 40717,
    "wmoNumber": 94592,
    "name": "COOLANGATTA",
    "state": "QLD",
    "latitude": -28.1681,
    "longitude": 153.5053
  },
  {
    "stationID": 40983,
    "wmoNumber": 95575,
    "name": "BEAUDESERT DRUMLEY STREET",
    "state": "QLD",
    "latitude": -27.9707,
    "longitude": 152.9898
  }
]
```

To remove, simply delete the specific object from the file.

###Troubleshooting

#####ERROR: Weather Stations file could not be loaded!
Weather station file missing or corrupt. The most likley cause for this is a missing [.weather_stations.json](../.weather_stations.json) file.

#####ERROR: Unable to establish connection to BOM
Program is unable to connect to the BOM website. Please check that your internet connection is active, and that the program is not being blocked by a firewall or other software.

#####ERROR: Unable to save to favorites
May happen if the program is in a protected directory, ensure that you have file write permissions.

##Building:
This project requires java 1.8 or later to build.

###Dependencies:
- [GSON](https://github.com/google/gson)
- [JFreeChart](http://www.jfree.org/jfreechart/)
- [JUnit](http://junit.org/) (testing)
- [AssertJ Swing JUnit](http://mvnrepository.com/artifact/org.assertj/assertj-swing-junit) (testing)
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) (testing)
- [LOGback](http://logback.qos.ch/) (logging)

If gradle has not been set up, run
```bash
$ ./grawdlew init
```
This will set up the gradle project.

To resolve dependencies, run

```bash
$ ./gradlew dependencies
```

Once the gradle wrapper has been initialized, to build the project, simply run
```bash
$ ./gradlew build
```

If the project has already been built, to execute the application, run
```bash
$ ./gradlew run
```

To assemble a standalone .jar, run

 ```bash
 $ ./gradlew shadowJar
 ```

###Testing:

Tests can be run with
```bash
$ ./gradlew test
```
