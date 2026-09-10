package com.WhatsApp.AdminUserChatMessage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationId;

    private String email;

    private String contact;

    private String emailOtp;

    private String contactOtp;

    private Boolean emailVerified;

    private Boolean contactVerified;

    private LocalDateTime emailOtpExpiresAt;

    private LocalDateTime contactOtpExpiresAt;

    private Integer emailAttempts;

    private Integer contactAttempts;

    private LocalDateTime createdAt;

}
