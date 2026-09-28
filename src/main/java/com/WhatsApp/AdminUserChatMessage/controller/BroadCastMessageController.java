package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import com.WhatsApp.AdminUserChatMessage.service.BroadCastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BroadCastMessageController {

    private final BroadCastService broadCastService;
    private final AuthenticationService authenticationService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    @PreAuthorize("hasRole('ADMIN')")
    public BroadCastMessageResponseDto broadCastMessage(BroadCastMessageRequestDto broadCastMessageRequestDto, Principal principal) throws AccessDeniedException {
        Long adminId = authenticationService.getAuthenticatedUserIdFromToken(principal);
        return broadCastService.sendMessage(broadCastMessageRequestDto, adminId);
    }
}
