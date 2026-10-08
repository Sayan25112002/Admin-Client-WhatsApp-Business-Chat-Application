package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallAnswerResponseDto {

    private Long senderId;

    private Long receiverId;

    private String answer;

    private String callId;
}
