package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import com.WhatsApp.AdminUserChatMessage.service.BroadCastService;
import jakarta.persistence.Access;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class PublicChatHistoryController {

    private final BroadCastService broadCastService;
    private final AuthenticationService authenticationService;

    @GetMapping("/public/messages")
    public List<BroadCastMessageResponseDto> getPublicMessages(Principal principal) {
        try {
            Long userId = authenticationService.getAuthenticatedUserIdFromToken(principal);
            return broadCastService.getPublicMessages(userId);
        } catch (RuntimeException e) {
            log.error("PUBLIC HISTORY FAILED", e);
            throw e;
        }
    }
}
