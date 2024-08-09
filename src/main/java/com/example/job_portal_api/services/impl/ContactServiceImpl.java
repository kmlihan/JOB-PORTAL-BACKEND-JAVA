package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.ContactRequest;
import com.example.job_portal_api.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(ContactRequest contactRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(contactRequest.getEmail());
        message.setTo("khaledmlihan497@gmail.com");
        message.setSubject("New Contact Form Submission");
        message.setText("Name: " + contactRequest.getName() + "\n" +
                "Email: " + contactRequest.getEmail() + "\n" +
                "Phone: " + contactRequest.getPhone() + "\n" +
                "Message: " + contactRequest.getMessage());
        mailSender.send(message);
    }
}
