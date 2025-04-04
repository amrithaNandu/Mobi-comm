package com.backend.mobicomm.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class EmailService 
{
    @Autowired
    private JavaMailSender mailSender;

    public void sendOTPEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Login");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);
    }
    
    public void sendPaymentSuccessEmail(String toEmail, Map<String, String> planDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Successful - Mobi-Comm");

        String emailText = String.format(
            "Dear Customer,\n\n" +
            "Your payment for the following plan was successful:\n\n" +
            "Plan Name: %s\n" +
            "Price: %s\n" +
            "Validity: %s\n" +
            "Data: %s\n" +
            "Calls: %s\n" +
            "SMS: %s\n\n" +
            "Thank you for choosing Mobi-Comm!\n",
            planDetails.get("name"),
            planDetails.get("price"),
            planDetails.get("validity"),
            planDetails.get("data"),
            planDetails.get("calls"),
            planDetails.get("sms")
        );

        message.setText(emailText);
        mailSender.send(message);
    }
}
