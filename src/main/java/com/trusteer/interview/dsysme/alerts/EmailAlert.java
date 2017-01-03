package com.trusteer.interview.dsysme.alerts;

import com.trusteer.interview.dsysme.utils.EmailSender;

import javax.mail.internet.InternetAddress;

/***
 * Send alert upon file modification to email receipts
 */
public class EmailAlert implements AlertNotifier {
    private EmailSender emailSender;
    private InternetAddress[] sendTo;

    public EmailAlert(EmailSender emailSender, String sendTo) throws Exception {
        this.emailSender = emailSender;
        this.sendTo = InternetAddress.parse(sendTo);
    }

    @Override
    public void notify(FileModifiedEvent event) {
        emailSender.sendMail(sendTo, "File Modified Alert", event.toString());
    }
}
