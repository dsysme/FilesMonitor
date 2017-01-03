package com.trusteer.interview.dsysme;

/**
 * Created by Sharon on 28/12/2016.
 */
public class EmailAlert implements AlertNotifier {
    private String email;

    public EmailAlert(String email) {
        this.email = email;
    }

    @Override
    public void notify(FileModifiedEvent event) {
        // TODO replace with real code
        //System.out.println(email + " " + event);
    }
}
