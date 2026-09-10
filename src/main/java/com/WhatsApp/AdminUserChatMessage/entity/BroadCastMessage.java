package com.WhatsApp.AdminUserChatMessage.entity;

import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class BroadCastMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonIgnore
    private User user;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private LocalDateTime createdAt;

}
