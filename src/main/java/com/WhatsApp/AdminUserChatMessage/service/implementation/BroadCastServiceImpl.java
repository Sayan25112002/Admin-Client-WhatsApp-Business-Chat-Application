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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BroadCastServiceImpl implements BroadCastService {

    private final BroadCastRepository broadCastRepository;
    private final BroadCastMapper broadCastMapper;
    private final UserRepository userRepository;

    @Override
    public BroadCastMessageResponseDto sendMessage(BroadCastMessageRequestDto broadCastMessageRequestDto, Long senderId) throws AccessDeniedException {
        User sender = userRepository.findById(senderId).orElseThrow(()-> new RuntimeException("User not found"));
        BroadCastMessage broadCastMessage = broadCastMapper.toBroadCastMessage(broadCastMessageRequestDto);
        broadCastMessage.setUser(sender);
        broadCastMessage.setMessageType(MessageType.CHAT);
        broadCastMessage.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(broadCastMessage);
        return broadCastMapper.toBroadCastMessageResponseDto(broadCastMessage);
    }

    @Override
    public List<BroadCastMessageResponseDto> getPublicMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        LocalDateTime joinedAt = user.getPublicChatJoinedAt();
        if(joinedAt==null){
            return List.of();
        }
        return broadCastRepository
                .findByCreatedAtAfterOrderByIdAsc(joinedAt)
                .stream()
                .map(broadCastMapper::toBroadCastMessageResponseDto)
                .toList();
    }

}
