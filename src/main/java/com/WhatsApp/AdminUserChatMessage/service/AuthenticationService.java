package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.LoginRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.requestDto.RegisterRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.LoginResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.RegisterResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;

import java.security.Principal;

public interface AuthenticationService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

    Long getAuthenticatedUserIdFromToken(Principal principal);

    Role getAuthenticatedRoleFromToken(Principal principal);

}
