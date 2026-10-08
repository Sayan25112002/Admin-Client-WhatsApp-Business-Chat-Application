package com.WhatsApp.AdminUserChatMessage.dto.requestDto;

import com.WhatsApp.AdminUserChatMessage.entity.type.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallStartRequestDto {

    private Long callerId;

    private String callerName;

    private String callId;

    private CallType callType;

}
