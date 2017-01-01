package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sharon on 01/01/2017.
 */
public class LogAlert implements AlertNotifier {
    private final static Logger logger = LoggerFactory.getLogger(FilesMonitor.class);
    @Override
    public void notify(FileModifiedEvent event) {
        logger.info(event.toString());
    }
}
