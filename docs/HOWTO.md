#HOWTO
##Dependencies:
- [GSON](https://github.com/google/gson)
- [JFreeChart](http://www.jfree.org/jfreechart/)
- [JUnit](http://junit.org/) (testing)
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) (testing)

##Installation
To install and compile this software, a plugin called "Gradle" must be used. They way in which this plugin works changes
between IDE's. Notes on how to do this are contained below.

To use this only as an application, the user only needs to run the supplied "Weather-Application" JAR file.

##Usage
notes

##Building:

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
