package com.abc.api.services;

import com.abc.api.payload.request.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmail(String to, String subject, EmailRequest request) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<p style=\"font-size: 16px; padding-left: 18px;\">" + request.getTitle() + "</p>"
                + "<div style=\"background-color: #fff; padding:20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + request.getContent()
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlMessage, true);

        emailSender.send(message);
    }
}
