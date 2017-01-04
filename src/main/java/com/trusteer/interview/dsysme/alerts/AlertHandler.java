package com.trusteer.interview.dsysme.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/***
 * This class is responsible for executing the logic associated with file modifications.
 * It listens to file modification events and keeps a list of AlertNotifier.
 * AlertNotifier encapsulate the logic to be executed upon file modification. When a monitored file was found to be
 * modified, AlertHandler will call all AlertNotifiers to trigger alert.
 */
public class AlertHandler extends ArrayList<AlertNotifier> implements Observer {

    final static Logger logger = LoggerFactory.getLogger(AlertHandler.class);


    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof FileModifiedEvent))
            return;
        FileModifiedEvent event = (FileModifiedEvent)arg;
        stream().forEach(alertNotifier -> alertNotifier.notify(event));
        logger.info(event.toString());
    }
}
