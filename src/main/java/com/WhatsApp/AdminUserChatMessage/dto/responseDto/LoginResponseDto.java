package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpResponseDto {

    private String message;

    private Boolean verified;

    private Long userId;

    private String name;

    private String email;

    private String contact;

    private Role role;

}
