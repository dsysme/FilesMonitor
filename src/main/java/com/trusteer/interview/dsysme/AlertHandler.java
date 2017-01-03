package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sharon on 28/12/2016.
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
