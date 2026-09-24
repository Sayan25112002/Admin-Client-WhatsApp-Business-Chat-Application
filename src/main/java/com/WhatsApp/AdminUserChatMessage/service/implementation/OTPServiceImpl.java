package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.entity.OtpVerification;
import com.WhatsApp.AdminUserChatMessage.exception.InvalidOtpException;
import com.WhatsApp.AdminUserChatMessage.repository.OtpVerificationRepository;
import com.WhatsApp.AdminUserChatMessage.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final OtpVerificationRepository otpVerificationRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateOtp(String email, String contact) {
        String emailOtp = generateSixDigitsOTP();
        String contactOtp = generateSixDigitsOTP();
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setVerificationId(UUID.randomUUID().toString());
        otpVerification.setEmail(email);
        otpVerification.setContact(contact);
        otpVerification.setEmailOtp(emailOtp);
        otpVerification.setContactOtp(contactOtp);
        otpVerification.setEmailVerified(false);
        otpVerification.setContactVerified(false);
        otpVerification.setEmailOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpVerification.setContactOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpVerification.setEmailAttempts(0);
        otpVerification.setContactAttempts(0);
        otpVerification.setCreatedAt(LocalDateTime.now());
        otpVerificationRepository.save(otpVerification);
        return otpVerification.getVerificationId();
    }

    private String generateSixDigitsOTP() {
        return String.format("%06d", secureRandom.nextInt(100000));
    }

    @Override
    public void validateEmailOtp(String verificationId, String otp) {
        if(verificationId == null || verificationId.isBlank()){
            throw new InvalidOtpException("Verification Id is mandatory");
        }
        if(otp == null || otp.isBlank()){
            throw new InvalidOtpException("Email OTP is required");
        }
        OtpVerification otpVerification = otpVerificationRepository
                .findByVerificationId(verificationId)
                .orElseThrow(()-> new InvalidOtpException("OTP record not found"));
        if(Boolean.TRUE.equals(otpVerification.getEmailVerified())){
            throw new InvalidOtpException("Email is already verified");
        }
        if(!otpVerification.getEmailOtpExpiresAt().isAfter(LocalDateTime.now())){
            throw new InvalidOtpException("Email OTP expired. Please request a new one");
        }
        if(!otpVerification.getEmailOtp().equals(otp)){
            otpVerification.setEmailAttempts(otpVerification.getEmailAttempts() + 1);
            otpVerificationRepository.save(otpVerification);
            throw new InvalidOtpException("Invalid Email OTP");
        }
        otpVerification.setEmailVerified(true);
        otpVerificationRepository.save(otpVerification);
    }

    @Override
    public void validateContactOtp(String verificationId, String otp) {
        if(verificationId == null || verificationId.isBlank()){
            throw new InvalidOtpException("Verification Id is required");
        }
        if(otp == null || otp.isBlank()){
            throw new InvalidOtpException("Contact OTP is required");
        }
        OtpVerification otpVerification = otpVerificationRepository
                .findByVerificationId(verificationId)
                .orElseThrow(()-> new InvalidOtpException("OTP record not found"));
        if(Boolean.TRUE.equals(otpVerification.getContactVerified())){
            throw new InvalidOtpException("Contact is already verified");
        }
        if(!otpVerification.getContactOtpExpiresAt().isAfter(LocalDateTime.now())){
            throw new InvalidOtpException("Contact OTP expired. Please request a new one");
        }
        if(!otpVerification.getContactOtp().equals(otp)){
            otpVerification.setContactAttempts(otpVerification.getContactAttempts() + 1);
            otpVerificationRepository.save(otpVerification);
            throw new InvalidOtpException("Invalid Contact OTP");
        }
        otpVerification.setContactVerified(true);
        otpVerificationRepository.save(otpVerification);
    }
}
