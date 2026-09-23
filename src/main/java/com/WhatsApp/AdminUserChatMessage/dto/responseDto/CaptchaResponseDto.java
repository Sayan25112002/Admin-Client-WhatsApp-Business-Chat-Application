package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaptchaResponseDto {

    private String captchaId;

    private String captchaAnswer;

    private String captchaImage;

}
