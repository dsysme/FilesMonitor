package com.trusteer.interview.dsysme;

/**
 * Created by Sharon on 28/12/2016.
 */
public class AlertByEmail implements AlertNotifier {
    private String email;

    public AlertByEmail(String email) {
        this.email = email;
    }

    @Override
    public void notify(FileModifiedEvent event) {
        // TODO replace with real code
        //System.out.println(email + " " + event);
    }
}
