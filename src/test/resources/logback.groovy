import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG

CONSOLE = "CONSOLE"
FILE = "FILE"

appender(CONSOLE, ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{10}: %msg%n"
    }
}

appender(FILE, FileAppender) {
    file = "weather-app.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{50}: %msg%n"
    }
}

root(DEBUG, [CONSOLE, FILE])
