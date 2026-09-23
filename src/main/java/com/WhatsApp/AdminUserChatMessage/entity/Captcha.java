package com.WhatsApp.AdminUserChatMessage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String captchaId;

    @Column(nullable = false)
    private String captchaAnswer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String captchaImage;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean used = false;

    @Column(nullable = false)
    private Boolean isValid = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
