package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ConversationResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.MemberResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.service.PrivateChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatMessageController {

    private final PrivateChatService privateChatService;

    @GetMapping("/members")
    public List<MemberResponseDto> getMembers(){
        return privateChatService.getMembers();
    }

    @PostMapping("/private/conversation")
    public ConversationResponseDto openConversation(@AuthenticationPrincipal User user, @RequestParam(required = false) Long clientId){
        return privateChatService.openConversation(user,clientId);
    }

    @GetMapping("/private/{conversationId}/messages")
    public List<ChatMessageResponseDto> getMessages(@AuthenticationPrincipal User user, @PathVariable Long conversationId){
        return privateChatService.getMessages(user,conversationId);
    }

}
