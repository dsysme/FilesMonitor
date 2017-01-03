package com.trusteer.interview.dsysme.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/***
 * Responsible for sending email from 'username' account (must be gmail account)
 * https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 *
 */
public class EmailSender {

    final private String username;
    final private String password;
    final private Properties props;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

    }

    public void sendMail(InternetAddress[] sendTo, String subject, String text) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, sendTo);
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
