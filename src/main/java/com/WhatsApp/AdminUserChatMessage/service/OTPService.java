package com.WhatsApp.AdminUserChatMessage.service;

public interface OTPService {

    String generateOtp(String email, String contact);

    void validateEmailOtp(String verificationId, String otp);

    void validateContactOtp(String verificationId, String otp);

}
