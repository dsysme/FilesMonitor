package com.trusteer.interview.dsysme;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sharon on 28/12/2016.
 */
public class AlertHandler extends ArrayList<AlertNotifier> implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof FileModifiedEvent))
            return;
        FileModifiedEvent event = (FileModifiedEvent)arg;
        stream().forEach(alertNotifier -> alertNotifier.notify(event));
    }
}
