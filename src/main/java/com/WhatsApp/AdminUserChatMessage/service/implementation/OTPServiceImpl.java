package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.repository.OtpVerificationRepository;
import com.WhatsApp.AdminUserChatMessage.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OtpVerificationRepository otpVerificationRepository;

    @Override
    public String generateOtp(String userName) {
        return "";
    }

    @Override
    public String validateOtpAndGetUserName(String preAuthToken, String otp) {
        return "";
    }
}
