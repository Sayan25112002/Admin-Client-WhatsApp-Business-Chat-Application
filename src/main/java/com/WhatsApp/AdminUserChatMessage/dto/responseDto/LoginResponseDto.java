package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.entity.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private String message;

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private Boolean verified;

    private Long userId;

    private String name;

    private String email;

    private String contact;

    private Role role;

    private UserStatus userStatus;

}
