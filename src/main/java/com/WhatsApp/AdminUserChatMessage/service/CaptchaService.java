package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.CaptchaResponseDto;

public interface CaptchaService {

    CaptchaResponseDto generateImageCaptcha();

    void validateCaptcha(String captchaId, String captchaAnswer);

}
