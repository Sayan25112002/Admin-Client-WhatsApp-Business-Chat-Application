package com.WhatsApp.AdminUserChatMessage.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallAcceptRequestDto {

    private Long callerId;

    private Long acceptorId;

    private String callId;

}
