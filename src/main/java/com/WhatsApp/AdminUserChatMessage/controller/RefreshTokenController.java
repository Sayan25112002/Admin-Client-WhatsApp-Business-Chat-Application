package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refresh-token")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/validate")
    public ResponseEntity<Void> validateRefreshToken(@RequestParam String refreshToken) {
        boolean valid = refreshTokenService.validateRefreshToken(refreshToken);
        if(!valid){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeRefreshToken(@RequestParam String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
        return ResponseEntity.ok().build();
    }

}
