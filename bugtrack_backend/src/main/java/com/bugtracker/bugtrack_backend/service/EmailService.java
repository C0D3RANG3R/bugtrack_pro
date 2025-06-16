// package com.bugtracker.bugtrack_backend.service;

// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class EmailService {

//     private final JavaMailSender mailSender;

//     public void sendWelcomeMail(String to, String username) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject("Welcome to BugTracker!");
//         message.setText("Hi " + username + ",\n\nYour account has been successfully created.\n\nRegards,\nBugTracker Team");
//         mailSender.send(message);
//     }
// }
