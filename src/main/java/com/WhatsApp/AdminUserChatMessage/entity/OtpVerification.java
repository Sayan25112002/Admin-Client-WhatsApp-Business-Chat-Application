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
    private String emailOtp;

    private Boolean emailVerified;

    private LocalDateTime emailOtpExpiresAt;

    private Integer emailAttempts;

    private LocalDateTime createdAt;

}
