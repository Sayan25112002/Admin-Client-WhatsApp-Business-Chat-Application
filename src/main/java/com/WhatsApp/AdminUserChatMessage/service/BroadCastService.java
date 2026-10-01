package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BroadCastService {

    BroadCastMessageResponseDto sendMessage(BroadCastMessageRequestDto broadCastMessageRequestDto, Long senderId) throws AccessDeniedException;

    List<BroadCastMessageResponseDto> getPublicMessages(Long userId);

}
