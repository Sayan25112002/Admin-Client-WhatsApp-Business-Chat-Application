package com.WhatsApp.AdminUserChatMessage.service;

public interface OTPService {

    String generateOtp(String userName);

    String validateOtpAndGetUserName(String preAuthToken, String otp);

}
