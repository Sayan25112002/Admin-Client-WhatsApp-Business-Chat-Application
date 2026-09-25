package com.WhatsApp.AdminUserChatMessage.service;

public interface OTPService {

    String generateOtp(String email);

    void validateEmailOtp(String verificationId, String otp);

}
