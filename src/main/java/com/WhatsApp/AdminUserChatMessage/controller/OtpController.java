package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OtpController {

    private final OTPService otpService;

    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        return ResponseEntity.ok(otpService.generateOtp(email));
    }

    @PostMapping("/validateEmailOtp")
    public ResponseEntity<Void> validateEmailOtp(@RequestParam String verificationId, @RequestParam String otp) {
        otpService.validateEmailOtp(verificationId, otp);
        return ResponseEntity.ok().build();
    }

}
