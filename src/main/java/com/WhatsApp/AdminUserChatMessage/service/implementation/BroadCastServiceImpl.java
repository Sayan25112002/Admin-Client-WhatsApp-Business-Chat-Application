package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.WhatsApp.AdminUserChatMessage.mapper.BroadCastMapper;
import com.WhatsApp.AdminUserChatMessage.repository.BroadCastRepository;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import com.WhatsApp.AdminUserChatMessage.service.BroadCastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BroadCastServiceImpl implements BroadCastService {

    private final BroadCastRepository broadCastRepository;
    private final BroadCastMapper broadCastMapper;
    private final UserRepository userRepository;

    @Override
    public BroadCastMessageResponseDto sendMessage(BroadCastMessageRequestDto broadCastMessageRequestDto, Long senderId) throws AccessDeniedException {
        User sender = userRepository.findById(senderId).orElseThrow(()-> new RuntimeException("User not found"));
        if(senderId!=1){
            throw new AccessDeniedException("Only Admin Can send message");
        }
        BroadCastMessage broadCastMessage = broadCastMapper.toBroadCastMessage(broadCastMessageRequestDto);
        broadCastMessage.setUser(sender);
        broadCastMessage.setMessageType(MessageType.CHAT);
        broadCastMessage.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(broadCastMessage);
        return broadCastMapper.toBroadCastMessageResponseDto(broadCastMessage);
    }

}
