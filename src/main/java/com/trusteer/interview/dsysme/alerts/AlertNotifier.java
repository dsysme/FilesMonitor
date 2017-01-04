package com.trusteer.interview.dsysme.alerts;


public interface AlertNotifier {

    void notify(FileModifiedEvent  event);
}
