package com.bugtracker.bugtrack_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "sagnik.papun02@gmail.com";

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = buildMessage(to, subject, body);
        mailSender.send(message);
    }

    private SimpleMailMessage buildMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    public void sendWelcomeMail(String to, String username) {
        String subject = "Welcome to BugTrack!";
        String body = String.format(
            "Hello %s,\n\nWelcome to BugTrack! We're excited to have you on board.\n\nBest regards,\nThe BugTrack Team",
            username
        );
        send(to, subject, body);
    }
}
