package com.WhatsApp.AdminUserChatMessage.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String verificationId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String emailOtp;

    @Column(nullable = false)
    private String contactOtp;

    private Boolean emailVerified;

    private Boolean contactVerified;

    private LocalDateTime emailOtpExpiresAt;

    private LocalDateTime contactOtpExpiresAt;

    private Integer emailAttempts;

    private Integer contactAttempts;

    private LocalDateTime createdAt;

}
