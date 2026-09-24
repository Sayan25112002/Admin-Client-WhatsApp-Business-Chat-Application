package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.LoginRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.requestDto.RegisterRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.LoginResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.RegisterResponseDto;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.ok(authenticationService.register(registerRequestDto));
    }

}
