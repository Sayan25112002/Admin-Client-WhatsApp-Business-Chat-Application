package com.WhatsApp.AdminUserChatMessage.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallOfferRequestDto {

    private Long senderId;

    private String senderName;

    private Long receiverId;

    private String offer;

    private String callId;

}
