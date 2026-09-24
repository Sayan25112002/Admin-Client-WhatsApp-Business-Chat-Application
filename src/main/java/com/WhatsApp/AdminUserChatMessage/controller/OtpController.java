package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OtpController {

    private final OTPService otpService;

    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String email, @RequestParam String contact) {
        return ResponseEntity.ok(otpService.generateOtp(email, contact));
    }

    @PostMapping("/validateEmailOtp")
    public ResponseEntity<Void> validateEmailOtp(@RequestParam String verificationId, @RequestParam String otp) {
        otpService.validateEmailOtp(verificationId, otp);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validateContactOtp")
    public ResponseEntity<Void> validateContactOtp(@RequestParam String verificationId, @RequestParam String otp) {
        otpService.validateContactOtp(verificationId, otp);
        return ResponseEntity.ok().build();
    }

}
