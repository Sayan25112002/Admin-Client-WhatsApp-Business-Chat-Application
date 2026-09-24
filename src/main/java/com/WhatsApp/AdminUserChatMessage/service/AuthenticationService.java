package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.LoginRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.requestDto.RegisterRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.LoginResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.RegisterResponseDto;

public interface AuthenticationService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

}
