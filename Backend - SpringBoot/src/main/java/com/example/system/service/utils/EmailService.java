package com.example.system.service.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${email.service.from}")
    private String fromEmail;

    public void triggerEmail(String to, String subject, String body) {
        Thread.startVirtualThread(() -> sendMail(to, subject, body));
    }

    private void sendMail(String to, String subject, String body) {
        log.info("Sending email to {} with subject {}", to, subject);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(message);
            log.info("Email sent successfully to {} with subject {}", to, subject);
        } catch (MessagingException e) {
            log.error("Error sending email to {} due to error : {}", to, e.getMessage());
        }
    }
}
