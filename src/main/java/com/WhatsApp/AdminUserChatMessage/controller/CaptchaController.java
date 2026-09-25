package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.CaptchaResponseDto;
import com.WhatsApp.AdminUserChatMessage.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/generateImageCaptcha")
    public ResponseEntity<CaptchaResponseDto> generateImageCaptcha() {
        return ResponseEntity.ok(captchaService.generateImageCaptcha());
    }

    @PostMapping("/validateCaptcha")
    public ResponseEntity<Void> validateCaptcha(@RequestParam String captchaId, @RequestParam String captchaAnswer) {
        captchaService.validateCaptcha(captchaId, captchaAnswer);
        return ResponseEntity.ok().build();
    }
}
