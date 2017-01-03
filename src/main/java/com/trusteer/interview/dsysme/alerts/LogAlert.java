package com.trusteer.interview.dsysme.alerts;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import com.trusteer.interview.dsysme.FilesMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for logging FileModifiedEvents to a distinct log file $FILES_MONITOR_HOME/logs/FileModifications.log
 */
public class LogAlert implements AlertNotifier {

    private Logger logger;
    private String fileName;

    public LogAlert(String log, String fileName) {
        this.fileName = fileName;
        logger = createLoggerFor(log, this.fileName);
        LoggerFactory.getLogger(FilesMonitor.class).info("Created "+this.fileName+" for logging file modified events");
    }

    @Override
    public void notify(FileModifiedEvent event) {
        logger.info(event.toString());
    }

    /**
     * http://stackoverflow.com/questions/16910955/programmatically-configure-logback-appender
     * @param string
     * @param file
     * @return
     */
    private Logger createLoggerFor(String string, String file) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %logger{10} %msg%n");
        ple.setContext(lc);
        ple.start();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(string);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }
}
