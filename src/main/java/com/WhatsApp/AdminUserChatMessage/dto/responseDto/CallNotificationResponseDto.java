package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallNotificationResponseDto {

    private Long callerId;

    private String callerName;

    private Long userId;

    private String userName;

    private String status;

    private String callId;

}
