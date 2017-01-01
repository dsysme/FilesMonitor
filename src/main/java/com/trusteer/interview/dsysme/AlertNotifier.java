package com.trusteer.interview.dsysme;

/**
 * Created by Sharon on 28/12/2016.
 */
public interface AlertNotifier {

    void notify(FileModifiedEvent  event);
}
