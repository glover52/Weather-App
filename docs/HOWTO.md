#HOWTO

##Installation
To install and compile this software, a plugin called "Gradle" must be used. They way in which this plugin works changes
between IDE's. Notes on how to do this are contained below.

To use this only as an application, the user only needs to run the supplied "Weather-Application" JAR file.

##Usage

###The Interface:
#####*Main Window:*
![main_window_labeled](main_window_labeled.png)

1) **Search bar** - A search bar to filter the station list

2) **Station list** -  A list that shows all the known weather stations by default.

3) **Favorites panel** - You favorite stations will appear here for easy access.

#####*Details Window:*

![details_window_chart_labeled](details_window_chart_labeled.png)

4) **Favorite and refresh buttons** - Buttons to add the current station to favorites, or to refresh the observation data.

5) **Latest observation details** - The latest observations retrieved from BOM site.

6) **Temperature history chart** - A simple plot of the temperature change over time.

![details_window_table_labeled](details_window_table_labeled.png)

7) **Observation history table** -  A table containing detailed observation history for that station.

##Building:
Project requires java 1.8 or later to build.

###Dependencies:
- [GSON](https://github.com/google/gson)
- [JFreeChart](http://www.jfree.org/jfreechart/)
- [JUnit](http://junit.org/) (testing)
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) (testing)

If gradle has not been set up, run
```bash
$ ./grawdlew init
```
This will set up the gradle project. if you do not have the required dependencies installed run

```bash
$ ./gradlew dependencies
```


Once the gradle wrapper has been initialized simply run
```bash
$ ./gradlew build
```
to build the project.

##Testing:

Tests can be run with
```bash
$ ./gradlew test
```
