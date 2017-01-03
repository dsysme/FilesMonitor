package com.trusteer.interview.dsysme.alerts;

import com.trusteer.interview.dsysme.utils.EmailSender;

import javax.mail.internet.InternetAddress;

/***
 * Send alert upon file modification to email receipt
 */
public class EmailAlert implements AlertNotifier {
    private EmailSender emailSender;
    private InternetAddress[] sendTo;

    public EmailAlert(String sendTo) throws Exception {
        this.emailSender = new EmailSender("dsysme.interview@gmail.com", "dontpanic");
        this.sendTo = InternetAddress.parse(sendTo);
    }

    @Override
    public void notify(FileModifiedEvent event) {
        emailSender.sendMail(sendTo, "File Modified Alert", event.toString());
    }
}
