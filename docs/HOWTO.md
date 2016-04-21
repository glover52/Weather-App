#HOWTO
##Dependencies:
- [GSON](https://github.com/google/gson)
- [JFreeChart](http://www.jfree.org/jfreechart/)
- [JUnit](http://junit.org/) (testing)
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) (testing)

##Installation
Notes

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

Tests can be ran with
```bash
$ ./gradlew test
```
