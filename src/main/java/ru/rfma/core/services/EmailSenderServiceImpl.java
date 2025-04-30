package ru.rfma.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(final String toEmail, final String subject, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("finance.management.app@gmail.com");
        message.setTo(toEmail);
        message.setText(text);
        message.setSubject(subject);
        mailSender.send(message);
    }

    public void sendNotification(final String toEmail, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("finance.management.app@gmail.com");
        message.setTo(toEmail);
        message.setText(text);
        message.setSubject("Notification from finance management app");
        mailSender.send(message);
    }
}
