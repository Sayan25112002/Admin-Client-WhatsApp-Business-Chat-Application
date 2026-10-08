package com.WhatsApp.AdminUserChatMessage.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IceCandidateRequestDto {

    private Long senderId;

    private String senderName;

    private Long receiverId;

    private String candidate;

    private String callId;

}
