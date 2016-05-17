import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO

CONSOLE = "CONSOLE"
FILE = "FILE"

appender(CONSOLE, ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{20} - %msg%n"
    }
}

appender(FILE, FileAppender) {
    file = "weather-app.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
    }
}

logger("com.javacodegeeks.examples.logbackexample.beans", INFO)
root(DEBUG, [CONSOLE, FILE])
