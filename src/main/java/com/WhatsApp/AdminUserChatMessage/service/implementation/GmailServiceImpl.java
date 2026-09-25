package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.service.GmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailServiceImpl implements GmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("WhatsApp Email Verification Code");
        message.setText("Welcome to WhatsApp Email Verification Code.\n"
                +"Your OTP code for Email Verification code is " + otp + "\n"
                +"Thank You"
        );
        javaMailSender.send(message);
    }
}
